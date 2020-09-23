package ru.job4j.accident.service;

import ru.job4j.accident.repository.Store;

import java.util.Collection;

/**
 * BaseService.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public abstract class BaseService<T> implements IService<T> {

    private Store<T> dao;

    private String className;

    public BaseService(Store<T> dao, String className) {
        this.dao = dao;
        this.className = className;
    }

    @Override
    public T add(T element) {
        Object id = dao.add(element);
        T el;
        try {
            el = this.dao.getElementById((long) id, this.className);
        } catch (Exception e) {
            return element;
        }
        return el;
    }

    @Override
    public void delete(T element) {
        this.dao.delete(element);
    }

    @Override
    public void update(T element) {
        this.dao.update(element);
    }

    @Override
    public T getElementById(long id) {
        return this.dao.getElementById(id, this.className);
    }

    @Override
    public Collection<T> getList() {
        return this.dao.getList(this.className);
    }
}