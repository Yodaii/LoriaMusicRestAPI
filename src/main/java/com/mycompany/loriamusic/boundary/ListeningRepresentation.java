package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.BanDAO;
import com.mycompany.loriamusic.DAO.LikeDAO;
import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.Baned;
import com.mycompany.loriamusic.entity.Liked;
import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Rest controller for calls on listening URL 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@RestController
@RequestMapping(value = "/listening", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Listening.class)
public class ListeningRepresentation {

    @Autowired
    ListeningDAO listeningDao;

    @Autowired
    SessionUserDAO sessionUserDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    TrackDAO trackDao;

    @Autowired
    BanDAO banDao;

    @Autowired
    LikeDAO likeDao;
    
    /**
     * Method which set the play duration to the selected listening
     * @param idUser: user id of the listening
     * @param idTrack: track id of the listening
     * @param timeListen: play duration
     * @return NO CONTENT
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value = "/{idUser}/{idTrack}/{timeListen}")
    public ResponseEntity<?> updateListening(@PathVariable("idUser") String idUser, @PathVariable("idTrack") String idTrack, @PathVariable("timeListen") long timeListen) {
        User user = userDao.getById(idUser);
        Track track = trackDao.getById(idTrack);
        
        SessionUser sessListening = sessionUserDao.getCurrentSession(user);

        Listening listening = listeningDao.getListeningTrackSession(sessListening, track);
        
        listening.setDuration(timeListen);
        listeningDao.update(listening); 
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Method which set if the track is like or not by the user to the selected listening and create a Liked or Baned entry
     * @param idUser: user id of the listening
     * @param idTrack: track id of the listening
     * @param like: boolean who indicate if the track is like or not
     * @return NO CONTENT
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping(value = "/{idUser}/{idTrack}/{like}")
    public ResponseEntity<?> updateListening(@PathVariable("idUser") String idUser, @PathVariable("idTrack") String idTrack, @PathVariable("like") String like) {
        User user = userDao.getById(idUser);
        Track track = trackDao.getById(idTrack);

        SessionUser sessListening = sessionUserDao.getCurrentSession(user);

        Listening listening = listeningDao.getListeningTrackSession(sessListening, track);

        if (like.equals("true")) {
            listening.setLiked(true);
            Liked liked = new Liked(); 
            liked.setTimestamp(new Date());
            liked.setTrack(track);
            liked.setUser(user);
            likeDao.create(liked);
        } else {
            listening.setLiked(false);
            Baned ban = new Baned();
            ban.setTimestamp(new Date());
            ban.setTrack(track);
            ban.setUser(user);
            banDao.create(ban);
        }
        
        listeningDao.update(listening);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
