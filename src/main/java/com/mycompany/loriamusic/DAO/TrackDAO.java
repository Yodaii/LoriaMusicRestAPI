package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Track;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Track
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Repository
@Transactional
public class TrackDAO {

    @Autowired
    SessionFactory sessionFactory;
    
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    /**
     * Save the user in the database.
     * @param track: the track to create
     * @return the track created
     */
    public Track create(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(track);
        return track;
    }

    /**
     * Delete the track from the database.
     * @param track: the track to delete
     * @return the track deleted
     */
    public Track delete(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(track);
        return track;
    }

    /**
     * Return all the tracks stored in the database.
     * @return the list of all the tracks
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Track").list();
    }

    /**
     * Return the track having the passed id.
     * @param id: the id of the track
     * @return the track selected
     */
    public Track getById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Track.class, id);
    }

    /**
     * Update the passed track in the database.
     * @param track: the track to update
     * @return the track updated
     */
    public Track update(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(track);
        return track;
    }

    /**
     * Return the track having the passed title and artist's name
     * @param titreTrack: the title of the track
     * @param nomArtist: the artist's name of the track
     * @return the track selected
     */
    public Track searchTrack(String titreTrack, String nomArtist) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT tr FROM Track tr WHERE tr.title=:title AND tr.artist.name=:artist";
        Query query = currentSession.createQuery(hql);
        query.setParameter("title", titreTrack);
        query.setParameter("artist", nomArtist);
        
        if(query.list().isEmpty()){
            return null;
        }else{
            return (Track) query.list().get(0);
        }
    }
}
