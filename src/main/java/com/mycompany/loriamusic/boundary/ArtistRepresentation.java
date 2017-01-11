package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Artist;
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
@RequestMapping(value="/artist", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Artist.class)
public class ArtistRepresentation { 
    @Autowired
    ArtistResource ar;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllArtists(){
        Iterable<Artist> allArtists = ar.findAll();
        return new ResponseEntity<>(artistToResource(allArtists), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{artistid}")
    public ResponseEntity<?> getOneArtist(@PathVariable("artistid") Long id){
        return Optional.ofNullable(ar.findOne(id))
                .map(found -> new ResponseEntity(artistToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{artistid}")
    public ResponseEntity<?> updateArtist(@RequestBody Artist a, @PathVariable("artistid") Long id){
        a.setId_artist(id);
        Artist artist = ar.save(a);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveArtist(@RequestBody Artist a){
        Artist saved = ar.save(a);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(ArtistRepresentation.class)
                .slash(saved.getId_artist())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Artist>> artistToResource(Iterable<Artist> all){
        Link selfLink = linkTo(methodOn(ArtistRepresentation.class).getAllArtists())
                .withSelfRel();
        List<Resource<Artist>> artists = new ArrayList();
        all.forEach(artist -> artists.add(artistToResource(artist, false)));
        return new Resources<>(artists,selfLink);
    }
    
    private Resource<Artist> artistToResource(Artist a, Boolean collection){
        Link selfLink = linkTo(ArtistRepresentation.class)
                .slash(a.getId_artist())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(ArtistRepresentation.class).getAllArtists())
                    .withRel("collection");
            return new Resource<>(a, selfLink, collectionLink);
        } else {
            return new Resource<>(a, selfLink);
        }
    }
}