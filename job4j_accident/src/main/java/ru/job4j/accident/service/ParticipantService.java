package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Participant;
import ru.job4j.accident.repository.Store;

/**
 * ParticipantService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Service("participantService")
public class ParticipantService extends BaseService<Participant> {

    @Autowired
    public ParticipantService(Store<Participant> dao) {
        super(dao, "Participant");
    }
}