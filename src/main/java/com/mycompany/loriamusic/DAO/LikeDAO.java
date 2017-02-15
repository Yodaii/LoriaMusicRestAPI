package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.Liked;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Like
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Repository
@Transactional
public class LikeDAO {
    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the likes in the database.
     * @param like: the like to create
     * @return the like created
     */
    public Liked create(Liked like) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(like);
        return like;
    }

    /**
     * Delete the likes from the database.
     * @param like: the like to delete
     * @return the like deleted
     */
    public Liked delete(Liked like) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(like);
        return like;
    }

    /**
     * Return all the likes stored in the database.
     * @return the list of all the likes
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Like").list();
    }

    /**
     * Return the like having the passed id.
     * @param id: the id of the like
     * @return the like selected
     */
    public Liked getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Liked.class, id);
    }

    /**
     * Update the passed like in the database.
     * @param like: the like to update
     * @return the like updated
     */
    public Liked update(Liked like) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(like);
        return like;
    }
}
