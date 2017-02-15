package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.User;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class SessionUser
 * @author Yohann Vaubourg & Arthur Flambeau
 */
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
     * @param session: the session to create
     * @return the session created
     */
    public SessionUser create(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(session);
        return session;
    }

    /**
     * Delete the session from the database.
     * @param session: the session to delete
     * @return the session deleted
     */
    public SessionUser delete(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(session);
        return session;
    }

    /**
     * Return all the session stored in the database.
     * @return the list of all the sessions
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from SessionUser").list();
    }

    /**
     * Return the session having the passed id.
     * @param id: the id of the session
     * @return the session selected
     */
    public SessionUser getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(SessionUser.class, id);
    }

    /**
     * Update the passed session in the database.
     * @param session: the session to update
     * @return the session updated
     */
    public SessionUser update(SessionUser session) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(session);
        return session;
    }

    /**
     * Close current session of the user
     * @param user: the user of the session
     */
    public void endSessionUser(User user) {
        SessionUser session = getCurrentSession(user);
        session.setEnd_date(new Date());
        this.update(session);
    }

    /**
     * Return the current session of the passed user 
     * @param user: the user of the session
     * @return the session selected
     */
    public SessionUser getCurrentSession(User user) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT se FROM SessionUser se WHERE se.user=:user AND se.end_date IS NULL ORDER BY se.begin_date DESC";
        Query query = currentSession.createQuery(hql);
        query.setParameter("user", user);

        return (SessionUser) query.list().get(0);
    }
}
