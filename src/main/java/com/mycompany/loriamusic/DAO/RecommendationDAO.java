package com.mycompany.loriamusic.DAO;

import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.Recommendation;
import com.mycompany.loriamusic.entity.Track;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for the class Recommendation
 * @author Yohann Vaubourg & Arthur Flambeau
 */
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
     * @param recommendation: the recommendation to create
     * @return the recommendation created
     */
    public Recommendation create(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(recommendation);
        return recommendation;
    }

    /**
     * Save the collection of the recommendation in the database.
     * @param recos: the list of recommendation to create
     */
    public void createAll(List<Recommendation> recos) {
        for (Recommendation r : recos) {
            this.create(r);
        }
    }

    /**
     * Delete the recommendation from the database.
     * @param recommendation: the recommendation to delete
     * @return the recommendation deleted
     */
    public Recommendation delete(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(recommendation);
        return recommendation;
    }

    /**
     * Return all the recommendations stored in the database.
     * @return the list of all the recommendations
     */
    public List getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("from Recommendation").list();
    }

    /**
     * Return the recommendation having the passed id.
     * @param id: the id of the recommedation
     * @return the recommendation selected
     */
    public Recommendation getById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Recommendation.class, id);
    }

    /**
     * Update the passed recommendation in the database.
     * @param recommendation: the recommendation to update
     * @return the recommendation updated
     */
    public Recommendation update(Recommendation recommendation) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(recommendation);
        return recommendation;
    }

    /**
     * Update a recommendation to indicate that the user choose this recommedation
     * @param listening: the listening of the recommendation
     * @param track: the track of the recommendation
     * @param nomAlgo: the algorithm's name of the recommendation
     */
    public void setChooseRecommendation(Listening listening, Track track, String nomAlgo) {
        Recommendation reco = this.getRecommendationTrackListening(listening, track, nomAlgo);
        reco.setIsChoose(true);
        this.update(reco);
    }

    /**
     * Return the recommendation having the passed listening, track and algorithm's name
     * @param listening: the listening of the recommendation
     * @param track: the track of the recommendation
     * @param nomAlgo: the algorithm's name of the recommendation
     * @return the recommendation selected
     */
    public Recommendation getRecommendationTrackListening(Listening listening, Track track, String nomAlgo) {
        Session currentSession = sessionFactory.getCurrentSession();
        
        String hql = "SELECT re FROM Recommendation re WHERE re.track=:track AND re.listening=:listening AND re.nameAlgorithm='"+nomAlgo+"'";
        Query query = currentSession.createQuery(hql);
        query.setParameter("track", track);
        query.setParameter("listening", listening);

        return (Recommendation) query.list().get(0);
    }
}
