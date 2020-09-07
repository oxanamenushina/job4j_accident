package ru.job4j.accident.repository;

/**
 * TripleConsumer.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@FunctionalInterface
public interface TripleConsumer<T, V, K> {
    void accept(T t, V v, K k);
}