package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Ban;
import com.mycompany.loriamusic.entity.Genre;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BanDAO {
    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the ban in the database.
     */
    public Ban create(Ban ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(ban);
        return ban;
    }

    /**
     * Delete the ban from the database.
     */
    public Ban delete(Ban ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(ban);
        return ban;
    }

    /**
     * Return all the genres stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Ban").list();
    }

    /**
     * Return the ban having the passed id.
     */
    public Ban getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Ban.class, id);
    }

    /**
     * Update the passed ban in the database.
     */
    public Ban update(Ban ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(ban);
        return ban;
    }
}
