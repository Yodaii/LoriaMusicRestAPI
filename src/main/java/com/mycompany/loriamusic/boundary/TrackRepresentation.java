package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.apicall.Spotify;
import com.mycompany.loriamusic.apicall.Youtube;
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
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.Track;
import com.mycompany.loriamusic.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Rest controller for calls on track URL 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
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

    /**
     * Method wich return the list of all the tracks
     * @return the list of all the tracks
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping
    public ResponseEntity<?> getAllTracks() {
        List<Track> allTracks = trackDao.getAll();
        return new ResponseEntity<>(allTracks, HttpStatus.OK);
    }

    /**
     * Method wich return the track for the passed artist name and track title
     * If a track or an artist doesn't exist in the database, this method call the class in the apicall package for collect the informations
     * After, it insert it in the database
     * With this data, the method create a new listening in the current session for the specific user
     * @param idUser: user id who want the track
     * @param nameArtist: artist name of the track
     * @param titleTrack: title track of the tracks
     * @param mode: mode use by the user
     * @return NOT FOUND if the track doesn't exist OK and the specific track oterwise
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value = "/{idUser}/{nameArtist}/{titleTrack}/{mode}")
    public ResponseEntity<?> getSearchTrack(@PathVariable("idUser") String idUser, @PathVariable("nameArtist") String nameArtist, @PathVariable("titleTrack") String titleTrack, @PathVariable("mode") String mode) {
        Spotify spotify = new Spotify();

        User user = userDao.getById(idUser);

        Artist artist = artistDao.getById(nameArtist);

        if (artist == null) {
            artist = new Artist();
            artist.setName(nameArtist);
            ArrayList data = spotify.getArtistMetadata(artist, nameArtist, titleTrack);
            artist = (Artist) data.get(1);

            Set<Genre> genresArtist = new HashSet<>();
            ArrayList<Genre> genres = (ArrayList<Genre>) data.get(0);
            if (genres != null) {
                for (Genre g : genres) {
                    Genre exist = genreDao.getById(g.getName());

                    if (exist == null) {
                        genreDao.create(g);
                    }

                    genresArtist.add(g);
                }
            }
            artist.setGenres(genresArtist);
            artistDao.create(artist);
        }

        Track track = trackDao.searchTrack(titleTrack, nameArtist);

        if (track == null) {
            if (Youtube.search(nameArtist + " " + titleTrack) != null) {
                track = new Track();
                track.setTitle(titleTrack);
                track.setArtist(artist);

                // id youtube
                track.setId_track(Youtube.search(nameArtist + " " + titleTrack));

                // info spotify
                track = spotify.getTrackMetadata(track, nameArtist, titleTrack);

                trackDao.create(track);
            }
        } else if (!track.getId_track().equals(Youtube.search(track.getId_track()))) {
            track.setId_track(Youtube.search(nameArtist + " " + titleTrack));
            trackDao.update(track);
        }

        SessionUser sessUser = sessionUserDao.getCurrentSession(user);
        Listening newListening = new Listening();
        newListening.setSession(sessUser);
        newListening.setTrack(track);
        newListening.setMode(mode);
        newListening.setTimestamp(new Date());
        listeningDao.create(newListening);

        return Optional.ofNullable(track)
                .map(found -> new ResponseEntity(found, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Method wich return a track with the passed id
     * @param id: track id of the track
     * @return NOT FOUND if the track doesn't exist OK and the track otherwise
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(value = "/{trackid}")
    public ResponseEntity<?> getOneTrack(@PathVariable("trackid") String id) {
        return Optional.ofNullable(trackDao.getById(id))
                .map(found -> new ResponseEntity(found, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
