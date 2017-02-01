package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Track;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
     */
    public Track create(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(track);
        return track;
    }

    /**
     * Delete the track from the database.
     */
    public Track delete(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(track);
        return track;
    }

    /**
     * Return all the tracks stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Track").list();
    }

    /**
     * Return the track having the passed id.
     */
    public Track getById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Track.class, id);
    }

    /**
     * Update the passed track in the database.
     */
    public Track update(Track track) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(track);
        return track;
    }

    public Track searchTrack(String titreTrack, String nomArtist) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT tr FROM Track tr WHERE tr.titre=:titre AND tr.artist.nom=:artist";
        Query query = currentSession.createQuery(hql);
        query.setParameter("titre", titreTrack);
        query.setParameter("artist", nomArtist);
        
        if(query.list().isEmpty()){
            return null;
        }else{
            return (Track) query.list().get(0);
        }
    }
}
