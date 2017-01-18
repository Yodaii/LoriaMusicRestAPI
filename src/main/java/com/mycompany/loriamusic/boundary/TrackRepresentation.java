package com.mycompany.loriamusic.boundary;

import apicall.Spotify;
import apicall.Youtube;
import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Ecoute;
import com.mycompany.loriamusic.entity.Genre;
import com.mycompany.loriamusic.entity.Session;
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
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
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

@RestController
@RequestMapping(value = "/track", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Track.class)
public class TrackRepresentation {

    @Autowired
    TrackResource tr;

    @Autowired
    ArtistResource ar;

    @Autowired
    GenreResource gr;
    
    @Autowired
    UserResource ur;
    
    @Autowired
    SessionResource sr;
    
    @Autowired
    EcouteResource er;

    //GET
    @GetMapping
    public ResponseEntity<?> getAllTracks() {
        Iterable<Track> allTracks = tr.findAll();
        return new ResponseEntity<>(trackToResource(allTracks), HttpStatus.OK);
    }

    //GET une instance
    @GetMapping(value = "/{idUser}/{nomArtist}/{titreTrack}")
    public ResponseEntity<?> getSearchTrack(@PathVariable("idUser") String idUser, @PathVariable("nomArtist") String nomArtist, @PathVariable("titreTrack") String titreTrack) {
        Spotify spotify = new Spotify();
        
        User user = ur.findOne(idUser);
        
        Artist artist = ar.findOne(nomArtist);
        
        if (artist == null) {
            artist = new Artist();
            artist.setNom(nomArtist);
            ArrayList data = spotify.getArtistMetadata(artist, nomArtist, titreTrack);
            artist = (Artist) data.get(1);

            Set<Genre> genresArtist = new HashSet<Genre>();
            ArrayList<Genre> genres = (ArrayList<Genre>) data.get(0);
            if (genres != null) {
                for (Genre g : genres) {
                    Genre exist = gr.findOne(g.getNom());

                    if (exist == null) {
                        gr.save(g);
                    }

                    genresArtist.add(g);
                }
            }
            artist.setGenres(genresArtist);
            ar.save(artist);
        }

        Iterable<Track> tracks = tr.findAll();
        Track track = null;

        for (Track t : tracks) {
            if (t.getTitre().equals(titreTrack) && t.getArtist().getNom().equals(nomArtist)) {
                track = t;
                break;
            }
        }
        if (track == null) {
            if (Youtube.search(nomArtist + " " + titreTrack) != null) {
                track = new Track();
                track.setTitre(titreTrack);
                track.setArtist(artist);

                // id youtube
                track.setId_track(Youtube.search(nomArtist + " " + titreTrack));

                // info spotify
                track = spotify.getTrackMetadata(track, nomArtist, titreTrack);

                tr.save(track);
            }
        }

        List<Session> sessionsUser = sr.findAll();
        Session sessUser;
        for(Session s : sessionsUser){
            if(s.getDateFinn()==null && user.getEmail().equals(s.getUser().getEmail())){
                sessUser = s;
                Ecoute nvEcoute = new Ecoute();
                nvEcoute.setSession(sessUser);
                nvEcoute.setTrack(track);
                er.save(nvEcoute);
                break;
            }
        }
        
        return Optional.ofNullable(track)
                .map(found -> new ResponseEntity(trackToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //GET une instance
    @GetMapping(value = "/{trackid}")
    public ResponseEntity<?> getOneTrack(@PathVariable("trackid") String id) {
        return Optional.ofNullable(tr.findOne(id))
                .map(found -> new ResponseEntity(trackToResource(found, true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //UPDATE PUT
    @PutMapping(value = "/{trackid}")
    public ResponseEntity<?> updateTrack(@RequestBody Track t, @PathVariable("trackid") String id) {
        t.setId_track(id);
        Track track = tr.save(t);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //POST
    @PostMapping
    public ResponseEntity<?> saveTrack(@RequestBody Track t) {
        Track saved = tr.save(t);
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
