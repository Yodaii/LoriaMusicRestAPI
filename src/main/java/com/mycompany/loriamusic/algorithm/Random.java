package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Example of an algortim class
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Service
public class Random extends AbstractAlgorithm {
    /**
     * Return the recommendations for a specific user and track
     * @param user: an user
     * @param track: a track
     * @return the list of the calculated recommendations
     */
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

    /**
     * Return the name of the algorithm
     * @return the name of the algoritm
     */
    @Override
    public String getNameAlgo() {
        return "Random";
    }

}
