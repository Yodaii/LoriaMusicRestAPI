package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GenreDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the user in the database.
     */
    public Genre create(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(genre);
        return genre;
    }

    /**
     * Delete the genre from the database.
     */
    public Genre delete(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(genre);
        return genre;
    }

    /**
     * Return all the genres stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Genre").list();
    }

    /**
     * Return the genre having the passed id.
     */
    public Genre getById(String nom) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Genre.class, nom);
    }

    /**
     * Update the passed genre in the database.
     */
    public Genre update(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(genre);
        return genre;
    }
}
