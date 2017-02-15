package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.Track;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Listening
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Repository
@Transactional
public class ListeningDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the listening in the database.
     * @param listening: the listening to create
     * @return the listening created
     */
    public Listening create(Listening listening) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(listening);
        return listening;
    }

    /**
     * Delete the listening from the database.
     * @param listening: the listening to delte
     * @return the listening created
     */
    public Listening delete(Listening listening) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(listening);
        return listening;
    }

    /**
     * Return all the listening stored in the database.
     * @return the list of all the listening
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Listening").list();
    }

    /**
     * Return the listening having the passed id.
     * @param id: the id of the listening
     * @return the listening selected
     */
    public Listening getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Listening.class, id);
    }

    /**
     * Update the passed listening in the database.
     * @param listening: the listening to updated
     * @return the listening updated
     */
    public Listening update(Listening listening) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(listening);
        return listening;
    }
    
    /**
     * Return the listening having the passed session and track
     * @param session: the session of the listening
     * @param track: the track of the listening
     * @return the listening selected
     */
    public Listening getListeningTrackSession(SessionUser session, Track track) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT li FROM Listening li WHERE li.track=:track AND li.session=:session ORDER BY li.id_listening DESC";
        Query query = currentSession.createQuery(hql);
        query.setParameter("track", track);
        query.setParameter("session", session);

        return (Listening) query.list().get(0);
    }
}
