package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class AlgorithmFactory {
    @Autowired
    private Random random;
    
    public List<Track> createAlgorithm(String nom, User user, Track track){
       
        if(nom.equals("random")){
            System.err.println("uezzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            return random.computeRecommendation(user, track);
        }
        
        return null;
    }
}
