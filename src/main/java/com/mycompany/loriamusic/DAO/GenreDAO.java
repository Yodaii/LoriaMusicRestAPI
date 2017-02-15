package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Genre;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Genre
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Repository
@Transactional
public class GenreDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the genre in the database.
     * @param genre: the genre to create
     * @return the genre created
     */
    public Genre create(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(genre);
        return genre;
    }

    /**
     * Delete the genre from the database.
     * @param genre: the genre to delete
     * @return the genre deleted
     */
    public Genre delete(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(genre);
        return genre;
    }

    /**
     * Return all the genres stored in the database.
     * @return the list of all the genre
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Genre").list();
    }

    /**
     * Return the genre having the passed id.
     * @param nom: the name of the genre
     * @return the genre selected
     */
    public Genre getById(String nom) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Genre.class, nom);
    }

    /**
     * Update the passed genre in the database.
     * @param genre: the genre to update
     * @return the genre updated
     */
    public Genre update(Genre genre) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(genre);
        return genre;
    }
}
