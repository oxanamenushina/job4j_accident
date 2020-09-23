package ru.job4j.accident.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * AccidentHibernate.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Repository("accidentHibernate")
@Transactional
public class AccidentHibernate<T> implements Store<T> {

    private static final Logger LOG = LogManager.getLogger(AccidentHibernate.class.getName());

    private SessionFactory sf;

    @Autowired
    public void setSf(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Object add(T element) {
        Session session = sf.getCurrentSession();
        Object id = session.save(element);
        LOG.info(element);
        return id;
    }

    @Override
    public void delete(T element) {
        Session session = sf.getCurrentSession();
            session.delete(element);
    }

    @Override
    public void update(T element) {
        Session session = sf.getCurrentSession();
            session.update(element);
    }

    @Override
    public T getElementById(long id, String className) {
        T elem = null;
        try {
            Session session = sf.getCurrentSession();
            Class cl = Class.forName("ru.job4j.accident.model." + className);
            elem = (T) session.get(cl, id);
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        return elem;
    }

    @Override
    public Collection<T> getList(String className) {
        String query = "from ru.job4j.accident.model." + className;
        Collection<T> list = null;
        try {
            Session session = sf.getCurrentSession();
            Class cl = Class.forName("ru.job4j.accident.model." + className);
            list = session.createQuery(query, cl).list();
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        return list;
    }
}