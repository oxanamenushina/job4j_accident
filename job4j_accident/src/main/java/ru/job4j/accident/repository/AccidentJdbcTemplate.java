package ru.job4j.accident.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.Participant;
import ru.job4j.accident.model.ParticipantStatus;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Predicate;

/**
 * AccidentJdbcTemplate.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Repository
public class AccidentJdbcTemplate implements IAccidentMem {

    private final JdbcTemplate jdbc;

    private String sql = "select a.id aid, a.name aname, a.address aaddress, a.description adescription, "
            + "p.id pid, p.name pname, p.address paddress, p.phone pphone, p.passport_data ppassport_data, "
            + "ps.name psname from accidents a "
            + "left join accidents_participants ap on a.id = ap.id_accident "
            + "left join participants p on ap.id_participant = p.id "
            + "left join participant_status ps on ap.id_status = ps.id";

    @Autowired
    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        Collection<String> ps = this.getAllParticipantStatus();
        if (ps == null || ps.isEmpty()) {
            this.fillParticipantStatus();
        }
    }

    @Override
    public void add(Accident accident) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into accidents (name, description, address) values (?, ?, ?)", new String[]{"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getDesc());
            ps.setString(3, accident.getAddress());
            return ps;
        }, kh);
        if (!accident.getParticipants().isEmpty()) {
            int aId = (int) kh.getKey();
            Collection<Participant> participants = this.getAllParticipants();
            for (Participant participant : accident.getParticipants()) {
                int pId = this.addParticipant(participant);
                this.addAccidentsParticipants(aId, pId, this.getParticipantStatusId(participant.getStatus().toString()));
            }
        }
    }

    @Override
    public void delete(int id) {
        this.deleteAccidentsParticipantsByAccidentId(id);
        jdbc.update("delete from accidents where id = ?", id);
        this.deleteParticipants();
    }

    @Override
    public void update(Accident accident) {
        jdbc.update("update accidents set name = ?, description = ?, address = ? where id = ?",
                accident.getName(), accident.getDesc(), accident.getAddress(), accident.getId());
        new ParticipantSetOperations().updateParticipantSet(accident);
    }

    @Override
    public Accident getElementById(int id) {
        String query = this.sql + " where a.id = " + id;
        return this.getAccidents(query).get(id);
    }

    @Override
    public Collection<Accident> getList() {
        return this.getAccidents(this.sql).values();
    }

    @Override
    public Collection<Participant> getAllParticipants() {
        String qr = "select p.id pid, p.name pname, p.address paddress, p.phone pphone, p.passport_data ppassport_data, "
                + "ps.name psname from participants p "
                + "inner join accidents_participants ap on p.id = ap.id_participant "
                + "inner join participant_status ps on ap.id_status = ps.id";
        Collection<Participant> list = jdbc.query(qr,
                (rs, row) -> new Participant(
                        rs.getInt("pid"),
                        rs.getString("pname"),
                        rs.getString("paddress"),
                        rs.getString("ppassport_data"),
                        rs.getString("pphone"),
                        ParticipantStatus.valueOf(rs.getString("psname")))
        );
        return list;
    }

    private void addAccidentsParticipants(int aId, int pId, int sId) {
        jdbc.update("insert into accidents_participants (id_accident, id_participant, id_status) values (?, ?, ?)",
                aId, pId, sId);
    }

    private int addParticipant(Participant participant) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into participants (name, address, passport_data, phone) values (?, ?, ?, ?)"
                            + " on conflict (passport_data) do update "
                            + "set name = excluded.name, address = excluded.address, phone = excluded.phone",
                    new String[]{"id"});
            ps.setString(1, participant.getName());
            ps.setString(2, participant.getAddress());
            ps.setString(3, participant.getPassportData());
            ps.setString(4, participant.getPhone());
            return ps;
        }, kh);
        return (int) kh.getKey();
    }

    private void fillParticipantStatus() {
        jdbc.update("insert into participant_status (name) values (?), (?)",
                "Stirrer", "Sufferer");
    }

    private Integer getParticipantStatusId(String status) {
        return jdbc.queryForObject("select id from participant_status where name = ?",
                new Object[]{status}, Integer.class);
    }

    private Map<Integer, Accident> getAccidents(String query) {
        Map<Integer, Accident> accidents = new HashMap<>();
        jdbc.query(query,
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("aid"));
                    accident.setName(rs.getString("aname"));
                    accident.setAddress(rs.getString("aaddress"));
                    accident.setDesc(rs.getString("adescription"));
                    Accident old = accidents.putIfAbsent(accident.getId(), accident);
                    accident = old != null ? old : accident;
                    int pId = rs.getInt("pid");
                    if (pId > 0) {
                        Participant participant = new Participant();
                        participant.setId(rs.getInt("pid"));
                        participant.setName(rs.getString("pname"));
                        participant.setAddress(rs.getString("paddress"));
                        participant.setPhone(rs.getString("pphone"));
                        participant.setPassportData(rs.getString("ppassport_data"));
                        participant.setStatus(ParticipantStatus.valueOf(rs.getString("psname")));
                        accident.getParticipants().add(participant);
                    }
                    return accident;
                });
        return accidents;
    }

    private void updateParticipant(Participant participant) {
        jdbc.update("update participants set name = ?, address = ?, passport_data = ?, phone = ? where id = ?",
                participant.getName(), participant.getAddress(), participant.getPassportData(), participant.getPhone(),
                participant.getId());
    }

    private void deleteParticipants() {
        String sql = "DELETE FROM participants USING participants AS p "
                + "LEFT OUTER JOIN accidents_participants AS ap ON p.id = ap.id_participant "
                + "WHERE participants.id = p.id AND ap.id_participant IS NULL";
        jdbc.update(sql);
    }

    private void deleteAccidentsParticipantsByAccidentId(int id) {
        jdbc.update("delete from accidents_participants where id_accident = ?", id);
    }

    private void deleteAccidentsParticipantsByPIdAndAId(int aId, int pId) {
        jdbc.update("delete from accidents_participants where id_accident = ? and id_participant = ?", aId, pId);
    }

    private void updateAccidentsParticipants(int aId, int pId, int sId) {
        jdbc.update("update accidents_participants set id_status = ? where id_accident = ? and id_participant = ?",
                sId, aId, pId);
    }

    private Collection<String> getAllParticipantStatus() {
        return jdbc.query("select name from participant_status", (rs, row) -> rs.getString("name"));
    }

    /**
     * ParticipantSetOperations.
     */
    private class ParticipantSetOperations {

        private void updateParticipantSet(Accident accident) {
            Set<Participant> old = getElementById(accident.getId()).getParticipants();
            Map<Predicate<Participant>, TripleConsumer<Accident, Participant, Set<Participant>>> operations =
                    this.getOperation(old);
            accident.getParticipants().forEach(p -> operations.entrySet().stream()
                    .filter(e -> e.getKey().test(p)).findFirst().get().getValue().accept(accident, p, old));
            if (!old.isEmpty()) {
                old.forEach(p -> deleteAccidentsParticipantsByPIdAndAId(accident.getId(), p.getId()));
                deleteParticipants();
            }
        }

        private Map<Predicate<Participant>, TripleConsumer<Accident, Participant, Set<Participant>>> getOperation(
                Set<Participant> old) {
            Map<Predicate<Participant>, TripleConsumer<Accident, Participant, Set<Participant>>> operations = new HashMap<>();
            operations.put(p -> !old.contains(p) && (p.getId() <= 0 || (p.getId() > 0 && isExist(p) && !isChange(old, p))),
                    this::addNewParticipantOperation);
            operations.put(p -> !old.contains(p) && p.getId() > 0 && !isExist(p), this::updateParticipantOperation);
            operations.put(p -> !old.contains(p) && p.getId() > 0 && isExist(p) && isChange(old, p),
                    this::changePassportDataOperation);
            operations.put(old::contains, this::deleteParticipantOperation);
            return operations;
        }

        private boolean isExist(Participant participant) {
            Collection<Participant> participants = getAllParticipants();
            return participants.stream().anyMatch(p -> participant.getPassportData().equals(p.getPassportData()));
        }

        private boolean isChange(Set<Participant> old, Participant participant) {
            return old.stream().anyMatch(p -> p.getId() == participant.getId()
                    && p.getPassportData().equals(participant.getPassportData()));
        }

        private void addNewParticipantOperation(Accident accident, Participant participant, Set<Participant> old) {
            int pId = addParticipant(participant);
            addAccidentsParticipants(accident.getId(), pId, getParticipantStatusId(participant.getStatus().toString()));
        }

        private void updateParticipantOperation(Accident accident, Participant participant, Set<Participant> old) {
            updateParticipant(participant);
            this.updateAccidentParticipantOperation(accident, participant, old);
        }

        private void changePassportDataOperation(Accident accident, Participant participant, Set<Participant> old) {
            addParticipant(participant);
            this.updateAccidentParticipantOperation(accident, participant, old);
        }

        private void deleteParticipantOperation(Accident accident, Participant participant, Set<Participant> old) {
            old.remove(participant);
        }

        private void updateAccidentParticipantOperation(Accident accident, Participant participant, Set<Participant> old) {
            updateAccidentsParticipants(accident.getId(), participant.getId(),
                    getParticipantStatusId(participant.getStatus().toString()));
            old.remove(old.stream().filter(p -> participant.getId() == p.getId()).findFirst().orElse(null));
        }
    }
}