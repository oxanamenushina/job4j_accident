package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.Participant;
import ru.job4j.accident.repository.IAccidentMem;

import java.util.Collection;
import java.util.Set;

/**
 * AccidentService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Service
public class AccidentService implements IAccidentService {

    private IAccidentMem accidentDAO;

    private final String nameTemplate = "[A-Za-zА-Яа-яЁё ]+";
    private final String phoneTemplate = "(\\+7|8)\\d{10}";
    private final String fieldTemplate = ".+";

    @Autowired
    @Qualifier("accidentJdbcTemplate")
    public void setAccidentDAO(IAccidentMem accidentDAO) {
        this.accidentDAO = accidentDAO;
    }

    @Transactional
    @Override
    public void add(Accident accident) {
        boolean result = this.validateAccident(accident);
        if (result) {
            this.accidentDAO.add(accident);
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        Accident accident = this.accidentDAO.getElementById(id);
        boolean result = accident != null;
        if (result) {
            this.accidentDAO.delete(id);
        }
    }

    @Transactional
    @Override
    public void update(Accident accident) {
        Accident accid = this.accidentDAO.getElementById(accident.getId());
        boolean result = accid != null && this.validateAccident(accident);
        if (result) {
            Set<Participant> old = this.accidentDAO.getElementById(accident.getId()).getParticipants();
            accident.getParticipants().stream().filter(p -> p.getId() <= 0).forEach(p -> {
                Participant oldP = old.stream().filter(par -> par.getPassportData().equals(p.getPassportData()))
                        .findFirst().orElse(null);
                p.setId(oldP != null ? oldP.getId() : p.getId());
            });
            this.accidentDAO.update(accident);
        }
    }

    @Transactional
    @Override
    public Accident getElementById(int id) {
        return this.accidentDAO.getElementById(id);
    }

    @Transactional
    @Override
    public Collection<Accident> getList() {
        return this.accidentDAO.getList();
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
}