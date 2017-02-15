package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.AlgorithmDAO;
import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.RecommendationDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.algorithm.AbstractAlgorithm;
import com.mycompany.loriamusic.algorithm.AlgorithmFactory;
import com.mycompany.loriamusic.entity.Algorithm;
import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.Recommendation;
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Rest controller for calls on reco URL 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@RestController
@RequestMapping(value = "/reco", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Recommendation.class)
public class RecommendationRepresentation {
    @Autowired
    RecommendationDAO recommendationDao;

    @Autowired
    SessionUserDAO sessionUserDao;

    @Autowired
    TrackDAO trackDao;

    @Autowired
    ListeningDAO listeningDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    AlgorithmDAO algoDao;
    
    @Autowired
    AlgorithmFactory algoFactory;

    /**
     * Method which return the recommendations of the differents algorithms in the database for a specific listening
     * @param idUser: user id who listen a track
     * @param idTrack: track id of the track listened
     * @return the list of recommendations
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value = "/{idUser}/{idTrack}")
    public ResponseEntity<?> getRecommendationsListening(@PathVariable("idUser") String idUser, @PathVariable("idTrack") String idTrack) {
        User user = userDao.getById(idUser);
        Track track = trackDao.getById(idTrack);
        SessionUser session = sessionUserDao.getCurrentSession(user);
        Listening listening = listeningDao.getListeningTrackSession(session, track);

        List<Algorithm> algorithms = algoDao.getAll();
        List<Recommendation> recoToReturn = new ArrayList<>();

        if (!algorithms.isEmpty()) {
            for (Algorithm algo : algorithms) {
                if (user != null && track != null) {
                    AbstractAlgorithm abstractAlgo = algoFactory.createAlgorithm(algo.getName());
                    List<Track> tracks = abstractAlgo.computeRecommendation(user, track);
                    if (!tracks.isEmpty()) {
                        for (Track t : tracks) {
                            Recommendation reco = new Recommendation();
                            reco.setListening(listening);
                            reco.setTrack(t);
                            reco.setIsChoose(false);
                            reco.setNameAlgorithm(abstractAlgo.getNameAlgo());
                            recommendationDao.create(reco);
                            recoToReturn.add(reco);
                        }
                    }
                }

            }
        }

        return new ResponseEntity<>(recoToReturn, HttpStatus.OK);
    }

    /**
     * Method which indicate if a recommendation has been selected by the user
     * @param idUser: user id of the listening
     * @param idTrackEcoute: track id of the track actually play
     * @param idTrackReco: track id of the recommendation clicked
     * @param nomAlgo: algorithm name of the recommendation
     * @return NO CONTENT
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value = "/{idUser}/{idTrackEcoute}/{idTrackReco}/{nomAlgo}")
    public ResponseEntity<?> updateRecommendation(@PathVariable("idUser") String idUser, @PathVariable("idTrackEcoute") String idTrackEcoute, @PathVariable("idTrackReco") String idTrackReco, @PathVariable("nomAlgo") String nomAlgo) {
        User user = userDao.getById(idUser);
        SessionUser sessUser = sessionUserDao.getCurrentSession(user);

        Track trackListening = trackDao.getById(idTrackEcoute);
        Listening listeningSess = listeningDao.getListeningTrackSession(sessUser, trackListening);

        Track trackRecommendation = trackDao.getById(idTrackReco);
        
        recommendationDao.setChooseRecommendation(listeningSess, trackRecommendation, nomAlgo);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
