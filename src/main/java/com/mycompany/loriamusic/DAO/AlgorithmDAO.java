package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Algorithm;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Algorithm
 * @author Yohann Vaubourg & Arthur Flambeau
 */
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
     * @param algo: the algorithm to create
     * @return the algorithm created
     */
    public Algorithm create(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(algo);
        return algo;
    }

    /**
     * Delete the algorithm from the database.
     * @param algo: the algorithm to delete
     * @return the algorithm deleted
     */
    public Algorithm delete(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(algo);
        return algo;
    }

    /**
     * Return all the algorithms stored in the database.
     * @return the list of all the algorithms
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Algorithm").list();
    }

    /**
     * Return the algorithm having the passed id.
     * @param id: the id of the algorithm
     * @return the algorithm selecteds
     */
    public Algorithm getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Algorithm.class, id);
    }

    /**
     * Update the passed algorithm in the database.
     * @param algo: the algorithm to update
     * @return the algorithm updated
     */
    public Algorithm update(Algorithm algo) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(algo);
        return algo;
    }
}
