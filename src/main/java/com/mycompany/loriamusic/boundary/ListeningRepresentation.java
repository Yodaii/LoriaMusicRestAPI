package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.Listening;
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
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping(value = "/ecoute", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Listening.class)
public class ListeningRepresentation {

    @Autowired
    ListeningDAO linsteningDao;

    @Autowired
    SessionUserDAO sessionUserDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    TrackDAO trackDao;

    //GET
    @GetMapping
    public ResponseEntity<?> getAllEcoutes() {
        Iterable<Listening> allEcoutes = linsteningDao.getAll();
        return new ResponseEntity<>(ecouteToResource(allEcoutes), HttpStatus.OK);
    }

    //GET une instance
    @GetMapping(value = "/{ecouteid}")
    public ResponseEntity<?> getOneEcoute(@PathVariable("ecouteid") Long id) {
        return Optional.ofNullable(linsteningDao.getById(id))
                .map(found -> new ResponseEntity(ecouteToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //UPDATE PUT
    @CrossOrigin(origins = "http://localhost:8081")
    @PutMapping(value = "/{idUser}/{idTrack}/{aime}")
    public ResponseEntity<?> updateEcoute(@PathVariable("idUser") String idUser, @PathVariable("idTrack") String idTrack, @PathVariable("aime") String aime) {
        User user = userDao.getById(idUser);
        Track track = trackDao.getById(idTrack);

        SessionUser sessEcoute = sessionUserDao.getCurrentSession(user);

        Listening ecoute = linsteningDao.getListeningTrackSession(sessEcoute, track);

        if (aime.equals("true")) {
            ecoute.setLiked(true);
        } else {
            ecoute.setLiked(false);
        }
        
        linsteningDao.update(ecoute);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //POST
    @PostMapping
    public ResponseEntity<?> saveEcoute(@RequestBody Listening e) {
        Listening saved = linsteningDao.create(e);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(ListeningRepresentation.class)
                .slash(saved.getId_listening())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    private Resources<Resource<Listening>> ecouteToResource(Iterable<Listening> all) {
        Link selfLink = linkTo(methodOn(ListeningRepresentation.class).getAllEcoutes())
                .withSelfRel();
        List<Resource<Listening>> ecoutes = new ArrayList();
        all.forEach(ecoute -> ecoutes.add(ecouteToResource(ecoute, false)));
        return new Resources<>(ecoutes, selfLink);
    }

    private Resource<Listening> ecouteToResource(Listening e, Boolean collection) {
        Link selfLink = linkTo(ListeningRepresentation.class)
                .slash(e.getId_listening())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(ListeningRepresentation.class).getAllEcoutes())
                    .withRel("collection");
            return new Resource<>(e, selfLink, collectionLink);
        } else {
            return new Resource<>(e, selfLink);
        }
    }
}
