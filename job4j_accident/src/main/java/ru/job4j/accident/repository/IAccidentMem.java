package ru.job4j.accident.repository;

import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.Participant;

import java.util.Collection;

/**
 * IAccidentMem.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface IAccidentMem {

    void add(Accident accident);

    void delete(int id);

    void update(Accident accident);

    Accident getElementById(int id);

    Collection<Accident> getList();

    Collection<Participant> getAllParticipants();
}