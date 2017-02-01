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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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

    //GET
    @GetMapping
    public ResponseEntity<?> getAllRecommendations() {
        Iterable<Recommendation> allRecommandations = recommendationDao.getAll();
        return new ResponseEntity<>(recommendationToResource(allRecommandations), HttpStatus.OK);
    }

    //GET une instance
    @GetMapping(value = "/{recoid}")
    public ResponseEntity<?> getOneRecommendation(@PathVariable("recoid") Long id) {
        return Optional.ofNullable(recommendationDao.getById(id))
                .map(found -> new ResponseEntity(recommendationToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //GET une instance
    @GetMapping(value = "/{idUser}/{idTrack}")
    public ResponseEntity<?> getRecommendationsListening(@PathVariable("idUser") String idUser, @PathVariable("idTrack") String idTrack) {
        User user = userDao.getById(idUser);
        Track track = trackDao.getById(idTrack);
        SessionUser session = sessionUserDao.getCurrentSession(user);
        Listening listening = listeningDao.getListeningTrackSession(session, track);

        List<Algorithm> algorithms = algoDao.getAll();
        List<Track> trackToReturn = new ArrayList<>();

        if (!algorithms.isEmpty()) {
            for (Algorithm algo : algorithms) {
                System.err.println(user);
                System.err.println(track);
                if (user != null && track != null) {
                    System.err.println(track.getTitre() + "|||||" + user.getEmail());
                    List<Track> tracks = algoFactory.createAlgorithm(algo.getNom(), user, track);
                    System.err.println(tracks);
                    if (!tracks.isEmpty()) {
                        trackToReturn.addAll(tracks);
                        for (Track t : tracks) {
                            Recommendation reco = new Recommendation();
                            reco.setEcoute(listening);
                            reco.setTrack(t);
                            reco.setEstChoisit(false);
                            recommendationDao.create(reco);
                        }
                    }
                }

            }
        }

        Iterable<Track> iterTracks = trackToReturn;
        return new ResponseEntity<>(trackToResource(iterTracks), HttpStatus.OK);
    }

    //UPDATE PUT
    @PutMapping(value = "/{idUser}/{idTrackEcoute}/{idTrackReco")
    public ResponseEntity<?> updateRecommendation(@PathVariable("idUser") String idUser, @PathVariable("idTrackEcoute") String idTrackEcoute, @PathVariable("idTrackReco") String idTrackReco) {
        User user = userDao.getById(idUser);
        SessionUser sessUser = sessionUserDao.getCurrentSession(user);

        Track trackListening = trackDao.getById(idTrackEcoute);
        Listening ecouteSess = listeningDao.getListeningTrackSession(sessUser, trackListening);

        Track trackRecommendation = trackDao.getById(idTrackReco);
        recommendationDao.setChooseRecommendation(ecouteSess, trackRecommendation);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //POST
    @PostMapping
    public ResponseEntity<?> saveRecommendation(@RequestBody Recommendation r) {
        Recommendation saved = recommendationDao.create(r);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(RecommendationRepresentation.class)
                .slash(saved.getId_reco())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    private Resources<Resource<Recommendation>> recommendationToResource(Iterable<Recommendation> all) {
        Link selfLink = linkTo(methodOn(RecommendationRepresentation.class).getAllRecommendations())
                .withSelfRel();
        List<Resource<Recommendation>> recommandations = new ArrayList();
        all.forEach(recommandation -> recommandations.add(recommendationToResource(recommandation, false)));
        return new Resources<>(recommandations, selfLink);
    }

    private Resource<Recommendation> recommendationToResource(Recommendation r, Boolean collection) {
        Link selfLink = linkTo(RecommendationRepresentation.class)
                .slash(r.getId_reco())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(RecommendationRepresentation.class).getAllRecommendations())
                    .withRel("collection");
            return new Resource<>(r, selfLink, collectionLink);
        } else {
            return new Resource<>(r, selfLink);
        }
    }

    private Resources<Resource<Track>> trackToResource(Iterable<Track> all) {
        Link selfLink = linkTo(methodOn(TrackRepresentation.class).getAllTracks())
                .withSelfRel();
        List<Resource<Track>> tracks = new ArrayList();
        all.forEach(track -> tracks.add(trackToResource(track, false)));
        return new Resources<>(tracks, selfLink);
    }

    private Resource<Track> trackToResource(Track t, Boolean collection) {
        Link selfLink = linkTo(TrackRepresentation.class)
                .slash(t.getId_track())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(TrackRepresentation.class).getAllTracks())
                    .withRel("collection");
            return new Resource<>(t, selfLink, collectionLink);
        } else {
            return new Resource<>(t, selfLink);
        }
    }
}
