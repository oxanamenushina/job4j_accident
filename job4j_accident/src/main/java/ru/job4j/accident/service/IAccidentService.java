package ru.job4j.accident.service;

import ru.job4j.accident.model.Accident;

import java.util.Collection;

/**
 * IAccidentService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface IAccidentService {

    void add(Accident accident);

    void delete(int id);

    void update(Accident accident);

    Accident getElementById(int id);

    Collection<Accident> getList();
}