package com.mycompany.loriamusic.boundary;

import apicall.Youtube;
import com.mycompany.loriamusic.entity.Artist;
import com.mycompany.loriamusic.entity.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@RequestMapping(value="/track", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Track.class)
public class TrackRepresentation { 
    @Autowired
    TrackResource tr;
    
    @Autowired
    ArtistResource ar;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllTracks(){
        Iterable<Track> allTracks = tr.findAll();
        return new ResponseEntity<>(trackToResource(allTracks), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{nomArtist}/{titreTrack}")
    public ResponseEntity<?> getSearchTrack(@PathVariable("nomArtist") String nomArtist, @PathVariable("titreTrack") String titreTrack){
        Artist artist = ar.findOne(nomArtist);
        if(artist == null){
            artist = new Artist();
            artist.setNom(nomArtist);
            ar.save(artist);
        }
        
        Iterable<Track> tracks = tr.findAll();
        Track track = null;
        
        for(Track t : tracks){
            if(t.getTitre().equals(titreTrack) && t.getArtist().getNom().equals(nomArtist)){
                track = t;
                break;
            }
        }
        if(track == null){
            if(Youtube.search(nomArtist + " " + titreTrack) != null){
                track = new Track();
                track.setTitre(titreTrack);
                track.setArtist(artist);
                track.setId_track(Youtube.search(nomArtist + " " + titreTrack));
                tr.save(track);
            }
        }
        
        return Optional.ofNullable(track)
                .map(found -> new ResponseEntity(trackToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //GET une instance
    @GetMapping(value="/{trackid}")
    public ResponseEntity<?> getOneTrack(@PathVariable("trackid") String id){
        return Optional.ofNullable(tr.findOne(id))
                .map(found -> new ResponseEntity(trackToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{trackid}")
    public ResponseEntity<?> updateTrack(@RequestBody Track t, @PathVariable("trackid") String id){
        t.setId_track(id);
        Track track = tr.save(t);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveTrack(@RequestBody Track t){
        Track saved = tr.save(t);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(TrackRepresentation.class)
                .slash(saved.getId_track())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Track>> trackToResource(Iterable<Track> all){
        Link selfLink = linkTo(methodOn(ArtistRepresentation.class).getAllArtists())
                .withSelfRel();
        List<Resource<Track>> tracks = new ArrayList();
        all.forEach(track -> tracks.add(trackToResource(track, false)));
        return new Resources<>(tracks,selfLink);
    }
    
    private Resource<Track> trackToResource(Track t, Boolean collection){
        Link selfLink = linkTo(TrackRepresentation.class)
                .slash(t.getId_track())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(TrackRepresentation.class).getAllTracks())
                    .withRel("collection");
            return new Resource<>(t, selfLink, collectionLink);
        } else {
            return new Resource<>(t, selfLink);
        }
    }
}