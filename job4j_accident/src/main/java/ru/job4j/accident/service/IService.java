package ru.job4j.accident.service;

import java.util.Collection;

/**
 * IService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface IService<T> {

    T add(T element);

    void delete(T element);

    void update(T element);

    T getElementById(long id);

    Collection<T> getList();
}