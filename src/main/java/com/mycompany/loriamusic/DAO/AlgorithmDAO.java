package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Algorithm;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AlgorithmDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the algorithm in the database.
     */
    public Algorithm create(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(algo);
        return algo;
    }

    /**
     * Delete the algorithm from the database.
     */
    public Algorithm delete(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(algo);
        return algo;
    }

    /**
     * Return all the algorithms stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Algorithme").list();
    }

    /**
     * Return the algorithm having the passed id.
     */
    public Algorithm getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Algorithm.class, id);
    }

    /**
     * Update the passed algorithm in the database.
     */
    public Algorithm update(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(algo);
        return algo;
    }
}
