package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.AccidentParticipant;
import ru.job4j.accident.repository.Store;

/**
 * AccidentParticipantService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Service("accidentParticipantService")
public class AccidentParticipantService extends BaseService<AccidentParticipant> {

    @Autowired
    public AccidentParticipantService(Store<AccidentParticipant> dao) {
        super(dao, "AccidentParticipant");
    }

    @Override
    public AccidentParticipant getElementById(long id) {
        return null;
    }
}