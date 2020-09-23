package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentParticipant;
import ru.job4j.accident.model.Participant;
import ru.job4j.accident.repository.Store;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * AccidentService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Service("accidentService")
public class AccidentService extends BaseService<Accident> {

    private IService<Participant> participantService;
    private IService<AccidentParticipant> apService;

    private final String nameTemplate = "[A-Za-zА-Яа-яЁё ]+";
    private final String phoneTemplate = "(\\+7|8)\\d{10}";
    private final String fieldTemplate = ".+";

    @Autowired
    public AccidentService(Store<Accident> accidentDAO) {
        super(accidentDAO, "Accident");
    }

    @Autowired
    @Qualifier("participantService")
    public void setParticipantService(IService<Participant> participantService) {
        this.participantService = participantService;
    }

    @Autowired
    @Qualifier("accidentParticipantService")
    public void setApService(IService<AccidentParticipant> apService) {
        this.apService = apService;
    }

    @Override
    public Accident add(Accident accident) {
        boolean result = this.validateAccident(accident);
        long aId = 0L;
        if (result) {
            Accident added = super.add(accident);
            aId = added.getId();
            accident.getParticipants().forEach(p -> {
                p.setId(0L);
                this.apService.add(AccidentParticipant.of(added,
                        this.saveOrUpdateParticipant(p, accident.getParticipants()), p.getStatus()));
            });
        }
        return this.getElementById(aId);
    }

    @Override
    public void delete(Accident accident) {
        if (accident != null && accident.getId() > 0) {
            accident.getAccidentParticipants().forEach(this::deleteLinkAndParticipant);
            super.delete(accident);
        }
    }

    @Override
    public void update(Accident accident) {
        boolean result = super.getElementById(accident.getId()) != null && this.validateAccident(accident);
        if (result) {
            super.update(accident);
            new ParticipantSetOperations(accident).updateParticipantSet();
        }
    }

    @Override
    public Accident getElementById(long id) {
        Accident accident = super.getElementById(id);
        this.fillParams(accident);
        return accident;
    }

    @Override
    public Collection<Accident> getList() {
        Collection<Accident> accidents = super.getList();
        accidents.forEach(this::fillParams);
        return accidents;
    }

    private void fillParams(Accident accident) {
        accident.getAccidentParticipants().forEach(ap -> ap.getParticipant().setStatus(ap.getStatus()));
        accident.setParticipants(accident.getAccidentParticipants()
                .stream().map(AccidentParticipant::getParticipant).collect(Collectors.toSet()));
    }

    private boolean validateAccident(Accident accident) {
        return accident != null && accident.getAddress() != null && accident.getAddress().matches(this.fieldTemplate)
                && accident.getName() != null && accident.getName().matches(this.fieldTemplate)
                && accident.getDesc() != null && accident.getDesc().matches(this.fieldTemplate)
                && this.validateParticipants(accident.getParticipants());
    }

    private boolean validateParticipants(Set<Participant> participants) {
        return participants != null
                && participants.stream().allMatch(
                        p -> p.getPassportData() != null && p.getPassportData().matches(this.fieldTemplate)
                                && p.getPhone() != null && p.getPhone().matches(this.phoneTemplate)
                                && p.getName() != null && p.getName().matches(this.nameTemplate)
                                && p.getAddress() != null && p.getAddress().matches(this.fieldTemplate)
        );
    }

    private long chooseId(Set<Participant> old, Participant participant, Set<Participant> pNew) {
        Participant oldP = old.stream().filter(par -> par.getPassportData().equals(participant.getPassportData()))
                .findFirst().orElse(null);
        return oldP == null || (oldP != null && pNew.stream().anyMatch(p -> oldP.getId() == p.getId()))
                ? participant.getId() : oldP.getId();
    }

    private void deleteLinkAndParticipant(AccidentParticipant ap) {
        this.apService.delete(ap);
        if (checkUsage(ap.getParticipant())) {
            this.participantService.delete(ap.getParticipant());
        }
    }

    private boolean checkUsage(Participant participant) {
        return this.apService.getList().stream().noneMatch(ap -> ap.getParticipant().equals(participant));
    }

    private Participant saveOrUpdateParticipant(Participant p, Set<Participant> pNew) {
        long pId = this.chooseId(new HashSet<>(this.participantService.getList()), p, pNew);
        p.setId(pId);
        Participant participant;
        if (pId <= 0) {
            participant = this.participantService.add(p);
        } else {
            this.participantService.update(p);
            participant = this.participantService.getElementById(pId);
        }
        return participant;
    }

    /**
     * ParticipantSetOperations.
     */
    public class ParticipantSetOperations {

        private Accident accident;
        private Accident aOld;

        public ParticipantSetOperations(Accident accident) {
            this.accident = accident;
            this.aOld = getElementById(accident.getId());
        }

        private void updateParticipantSet() {
            Set<Participant> old = aOld.getParticipants();
            accident.getParticipants().forEach(p -> p.setId(this.chooseIdOfUpdatedParticipant(p)));
            this.deleteLinks(aOld.getAccidentParticipants());
            Map<Predicate<Participant>, BiConsumer<Participant, Set<Participant>>> operations = this.getOperation(old);
            accident.getParticipants().forEach(p -> operations.entrySet().stream()
                    .filter(e -> e.getKey().test(p)).findFirst().get().getValue().accept(p, old));
            old.forEach(p -> deleteLinkAndParticipant(aOld.getAccidentParticipants().stream()
                    .filter(ap -> ap.getParticipant().equals(p)).findFirst().get()));
        }

        private Map<Predicate<Participant>, BiConsumer<Participant, Set<Participant>>> getOperation(Set<Participant> old) {
            Map<Predicate<Participant>, BiConsumer<Participant, Set<Participant>>> operations = new HashMap<>();
            operations.put(p -> !old.contains(p) && p.getId() <= 0, this::addNewParticipantOperation);
            operations.put(p -> !old.contains(p) && p.getId() > 0
                            && aOld.getParticipants().stream().noneMatch(par -> par.getId() == p.getId()),
                    this::addAccidentParticipantOperation);
            operations.put(p -> !old.contains(p) && p.getId() > 0
                            && aOld.getParticipants().stream().anyMatch(par -> par.getId() == p.getId()),
                    this::updateAccidentParticipantOperation);
            operations.put(old::contains, this::deleteParticipantOperation);
            return operations;
        }

        private boolean isExist(Participant participant) {
            Collection<Participant> participants = participantService.getList();
            return participants.stream().anyMatch(p -> participant.getPassportData().equals(p.getPassportData()));
        }

        private boolean isSame(Participant participant) {
            return aOld.getParticipants().stream().anyMatch(p -> p.getId() == participant.getId()
                    && p.getPassportData().equals(participant.getPassportData()));
        }

        private void addNewParticipantOperation(Participant participant, Set<Participant> old) {
            Participant p = participantService.add(participant);
            apService.add(AccidentParticipant.of(accident, p, participant.getStatus()));
        }

        private void deleteParticipantOperation(Participant participant, Set<Participant> old) {
            old.remove(participant);
        }

        private void addAccidentParticipantOperation(Participant participant, Set<Participant> old) {
            participantService.update(participant);
            apService.add(AccidentParticipant.of(accident, participant, participant.getStatus()));
            old.remove(old.stream().filter(p -> participant.getId() == p.getId()).findFirst().orElse(null));
        }

        private void updateAccidentParticipantOperation(Participant participant, Set<Participant> old) {
            AccidentParticipant ap = aOld.getAccidentParticipants().stream()
                    .filter(a -> a.getParticipant().getId() == participant.getId()).findFirst().orElse(null);
            if (ap != null && !participant.getStatus().equals(ap.getParticipant().getStatus())) {
                apService.delete(ap);
                this.addAccidentParticipantOperation(participant, old);
            } else {
                participantService.update(participant);
                old.remove(old.stream().filter(p -> participant.getId() == p.getId()).findFirst().orElse(null));
            }
        }

        private void deleteLinks(Set<AccidentParticipant> old) {
            old.stream().filter(ap -> this.accident.getParticipants().stream()
                    .noneMatch(p -> p.getId() == ap.getParticipant().getId())).forEach(ap -> apService.delete(ap));
        }

        private long chooseIdOfUpdatedParticipant(Participant participant) {
            return participant.getId() <= 0 ? chooseId(aOld.getParticipants(), participant, accident.getParticipants())
                    : isSame(participant) || !isExist(participant) ? participant.getId()
                    : participantService.getList().stream()
                    .filter(p -> participant.getPassportData().equals(p.getPassportData())).findFirst().get().getId();

        }
    }
}