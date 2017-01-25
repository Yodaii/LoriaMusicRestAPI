package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Artist;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ArtistDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the artist in the database.
     */
    public Artist create(Artist artist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(artist);
        return artist;
    }

    /**
     * Delete the artist from the database.
     */
    public Artist delete(Artist artist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(artist);
        return artist;
    }

    /**
     * Return all the artists stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Artist").list();
    }

    /**
     * Return the artist having the passed id.
     */
    public Artist getById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Artist.class, id);
    }

    /**
     * Update the passed artist in the database.
     */
    public Artist update(Artist artist) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(artist);
        return artist;
    }
}
