package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.Recommendation;
import com.mycompany.loriamusic.entity.SessionUser;
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
public class RecommendationDAO {

    @Autowired
    SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Save the recommendation in the database.
     */
    public Recommendation create(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(recommendation);
        return recommendation;
    }

    /**
     * Save the collection of the recommendation in the database.
     */
    public void createAll(List<Recommendation> recos) {
        for (Recommendation r : recos) {
            this.create(r);
        }
    }

    /**
     * Delete the recommendation from the database.
     */
    public Recommendation delete(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(recommendation);
        return recommendation;
    }

    /**
     * Return all the recommendations stored in the database.
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Recommendation").list();
    }

    /**
     * Return the recommendation having the passed id.
     */
    public Recommendation getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Recommendation.class, id);
    }

    /**
     * Update the passed recommendation in the database.
     */
    public Recommendation update(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(recommendation);
        return recommendation;
    }

    public void setChooseRecommendation(Listening listening, Track track) {
        Recommendation reco = this.getRecommendationTrackListening(listening, track);
        reco.setEstChoisit(true);
        this.update(reco);
    }

    public Recommendation getRecommendationTrackListening(Listening listening, Track track) {
        Session currentSession = sessionFactory.getCurrentSession();

        String hql = "SELECT re FROM Recommendation re WHERE re.track=:track AND re.listening=:listening";
        Query query = currentSession.createQuery(hql);
        query.setParameter("track", track);
        query.setParameter("listening", listening);

        return (Recommendation) query.list().get(0);
    }
}
