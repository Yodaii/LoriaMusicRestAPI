package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SessionUserDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the session in the database.
     */
    public SessionUser create(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(session);
        return session;
    }

    /**
     * Delete the session from the database.
     */
    public SessionUser delete(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(session);
        return session;
    }

    /**
     * Return all the session stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from SessionUser").list();
    }

    /**
     * Return the session having the passed id.
     */
    public SessionUser getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(SessionUser.class, id);
    }

    /**
     * Update the passed session in the database.
     */
    public SessionUser update(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(session);
        return session;
    }

    /*
    *  Close current session of the user
     */
    public void endSessionUser(User user) {
        SessionUser session = getCurrentSession(user);
        this.update(session);
    }

    public SessionUser getCurrentSession(User user) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT se FROM SessionUser se WHERE se.user=:user AND se.end_date IS NULL";
        Query query = currentSession.createQuery(hql);
        query.setParameter("user", user);

        return (SessionUser) query.list().get(0);
    }
}
