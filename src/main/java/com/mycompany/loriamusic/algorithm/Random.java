package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class Random extends AbstractAlgorithm {
    
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

    @Override
    public String getNameAlgo() {
        return "Random";
    }

}
