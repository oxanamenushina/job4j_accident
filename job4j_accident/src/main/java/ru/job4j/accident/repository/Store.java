package ru.job4j.accident.repository;

import java.util.Collection;

/**
 * Store.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public interface Store<T> {

    Object add(T element);

    void delete(T element);

    void update(T element);

    T getElementById(long id, String className);

    Collection<T> getList(String className);
}