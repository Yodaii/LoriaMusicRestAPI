package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.DAO.ArtistDAO;
import com.mycompany.loriamusic.DAO.GenreDAO;
import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.RecommendationDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Random extends AbstractAlgorithm {
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
    RecommendationDAO recommendatioDao;
    
    @Override
    public List<Track> computeRecommendation(User user, Track track) {
        List<Track> tracks = trackDao.getAll();

        List<Track> tracksToReturn = new ArrayList<>();

        if (!tracks.isEmpty()) {
            while (tracksToReturn.size() < 3 && tracksToReturn.size() != tracks.size()) {
                int random = (int)(Math.random()*(tracks.size() ));
                tracksToReturn.add(tracks.get(random));
            }
        }

        return tracksToReturn;
    }

}
