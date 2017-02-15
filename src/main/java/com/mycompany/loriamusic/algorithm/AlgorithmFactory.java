package com.mycompany.loriamusic.algorithm;

import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Factory class
 * @author Yohann Vaubourg & Arthur Flambeau
 */
public class AlgorithmFactory {
    @Autowired
    private Random random;
    
    public AbstractAlgorithm createAlgorithm(String name){
       
        if(name.equals("random")){
            return random;
        }
        
        return null;
    }
}
