package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.DAO.ArtistDAO;
import com.mycompany.loriamusic.DAO.BanDAO;
import com.mycompany.loriamusic.DAO.GenreDAO;
import com.mycompany.loriamusic.DAO.LikeDAO;
import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.RecommendationDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Abstract class of the algorithm's factory
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Service
public abstract class AbstractAlgorithm {
    @Autowired
    TrackDAO trackDao;

    @Autowired
    ArtistDAO artistDao;

    @Autowired
    GenreDAO genreDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    SessionUserDAO sessionUserDao;

    @Autowired
    ListeningDAO listeningDao;
    
    @Autowired
    BanDAO banDao;

    @Autowired
    LikeDAO likeDao;
 
    @Autowired
    RecommendationDAO recommendatioDao;
    
    /**
     * Return the recommendations for a specific user and track
     * @param user: an user
     * @param track: a track
     * @return the list of the calculated recommendations
     */
    public abstract List<Track> computeRecommendation(User user, Track track);
    
    /**
     * Return the name of the algorithm
     * @return the name of the algoritm
     */
    public abstract String getNameAlgo();
}
