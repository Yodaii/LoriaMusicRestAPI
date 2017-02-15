package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class User
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Repository
@Transactional
public class UserDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    /**
     * Save the user in the database.
     * @param user: the user to create
     * @return the user created
     */
    public User create(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(user);
        return user;
    }

    /**
     * Delete the user from the database.
     * @param user: the user to delete
     * @return the user deleted
     */
    public User delete(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(user);
        return user;
    }

    /**
     * Return all the users stored in the database.
     * @return the list of all the users
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from User").list();
    }

    /**
     * Return the user having the passed id.
     * @param email: the email of the user
     * @return user selected
     */
    public User getById(String email) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(User.class, email);
    }

    /**
     * Update the passed user in the database.
     * @param user: the user to update
     * @return the user updated
     */
    public User update(User user) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(user);
        return user;
    }
}
