package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Baned;
import com.mycompany.loriamusic.entity.Genre;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Ban
 * @author Yohann Vaubourg & Arthur Flambeau
 */
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
     * @param ban: the ban to create
     * @return the ban created
     */
    public Baned create(Baned ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(ban);
        return ban;
    }

    /**
     * Delete the ban from the database.
     * @param ban: the ban to delete
     * @return the ban deleted
     */
    public Baned delete(Baned ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(ban);
        return ban;
    }

    /**
     * Return all the genres stored in the database.
     * @return the list of all the bans
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Ban").list();
    }

    /**
     * Return the ban having the passed id.
     * @param id: the id of the ban
     * @return the ban selected
     */
    public Baned getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Baned.class, id);
    }

    /**
     * Update the passed ban in the database.
     * @param ban: the ban to update
     * @return the ban updated
     */
    public Baned update(Baned ban) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(ban);
        return ban;
    }
}
