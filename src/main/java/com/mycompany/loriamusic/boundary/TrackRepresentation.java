package com.mycompany.loriamusic.boundary;

import apicall.Spotify;
import apicall.Youtube;
import com.mycompany.loriamusic.DAO.ArtistDAO;
import com.mycompany.loriamusic.DAO.GenreDAO;
import com.mycompany.loriamusic.DAO.ListeningDAO;
import com.mycompany.loriamusic.DAO.RecommendationDAO;
import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.TrackDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Listening;
import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.Recommendation;
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
@RequestMapping(value = "/track", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Track.class)
public class TrackRepresentation {

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

    //GET
    @GetMapping
    public ResponseEntity<?> getAllTracks() {
        Iterable<Track> allTracks = trackDao.getAll();
        return new ResponseEntity<>(trackToResource(allTracks), HttpStatus.OK);
    }

    //GET une instance
    @GetMapping(value = "/{idUser}/{nomArtist}/{titreTrack}")
    public ResponseEntity<?> getSearchTrack(@PathVariable("idUser") String idUser, @PathVariable("nomArtist") String nomArtist, @PathVariable("titreTrack") String titreTrack) {
        Spotify spotify = new Spotify();

        User user = userDao.getById(idUser);

        Artist artist = artistDao.getById(nomArtist);

        if (artist == null) {
            artist = new Artist();
            artist.setNom(nomArtist);
            ArrayList data = spotify.getArtistMetadata(artist, nomArtist, titreTrack);
            artist = (Artist) data.get(1);

            Set<Genre> genresArtist = new HashSet<Genre>();
            ArrayList<Genre> genres = (ArrayList<Genre>) data.get(0);
            if (genres != null) {
                for (Genre g : genres) {
                    Genre exist = genreDao.getById(g.getNom());

                    if (exist == null) {
                        genreDao.create(g);
                    }

                    genresArtist.add(g);
                }
            }
            artist.setGenres(genresArtist);
            artistDao.create(artist);
        }

        Track track = trackDao.searchTrack(titreTrack, nomArtist);

        if (track == null) {
            if (Youtube.search(nomArtist + " " + titreTrack) != null) {
                track = new Track();
                track.setTitre(titreTrack);
                track.setArtist(artist);

                // id youtube
                track.setId_track(Youtube.search(nomArtist + " " + titreTrack));

                // info spotify
                track = spotify.getTrackMetadata(track, nomArtist, titreTrack);

                trackDao.create(track);
            }
        } else if (track.getId_track() != Youtube.search(track.getId_track())) {
            track.setId_track(Youtube.search(nomArtist + " " + titreTrack));
            trackDao.update(track);
        }

        SessionUser sessUser = sessionUserDao.getCurrentSession(user);
        Listening nvEcoute = new Listening();
        nvEcoute.setSession(sessUser);
        nvEcoute.setTrack(track);
        listeningDao.create(nvEcoute);

        List<Recommendation> recos = nvEcoute.calculRecommandation(idUser, trackDao.getAll());
        recommendatioDao.createAll(recos);

        return Optional.ofNullable(track)
                .map(found -> new ResponseEntity(trackToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //GET une instance
    @GetMapping(value = "/{trackid}")
    public ResponseEntity<?> getOneTrack(@PathVariable("trackid") String id) {
        return Optional.ofNullable(trackDao.getById(id))
                .map(found -> new ResponseEntity(trackToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //UPDATE PUT
    @PutMapping(value = "/{trackid}")
    public ResponseEntity<?> updateTrack(@RequestBody Track t, @PathVariable("trackid") String id) {
        t.setId_track(id);
        Track track = trackDao.update(t);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //POST
    @PostMapping
    public ResponseEntity<?> saveTrack(@RequestBody Track t) {
        Track saved = trackDao.create(t);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(TrackRepresentation.class)
                .slash(saved.getId_track())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    private Resources<Resource<Track>> trackToResource(Iterable<Track> all) {
        Link selfLink = linkTo(methodOn(ArtistRepresentation.class).getAllArtists())
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
