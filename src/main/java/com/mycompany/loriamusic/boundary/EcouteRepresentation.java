package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Ecoute;
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
@RequestMapping(value="/ecoute", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Ecoute.class)
public class EcouteRepresentation { 
    @Autowired
    EcouteResource er;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllEcoutes(){
        Iterable<Ecoute> allEcoutes = er.findAll();
        return new ResponseEntity<>(ecouteToResource(allEcoutes), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{ecouteid}")
    public ResponseEntity<?> getOneEcoute(@PathVariable("ecouteid") Long id){
        return Optional.ofNullable(er.findOne(id))
                .map(found -> new ResponseEntity(ecouteToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{ecouteid}")
    public ResponseEntity<?> updateEcoute(@RequestBody Ecoute e, @PathVariable("ecouteid") Long id){
        e.setId_ecoute(id);
        Ecoute ecoute = er.save(e);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveEcoute(@RequestBody Ecoute e){
        Ecoute saved = er.save(e);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(EcouteRepresentation.class)
                .slash(saved.getId_ecoute())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Ecoute>> ecouteToResource(Iterable<Ecoute> all){
        Link selfLink = linkTo(methodOn(EcouteRepresentation.class).getAllEcoutes())
                .withSelfRel();
        List<Resource<Ecoute>> ecoutes = new ArrayList();
        all.forEach(ecoute -> ecoutes.add(ecouteToResource(ecoute, false)));
        return new Resources<>(ecoutes,selfLink);
    }
    
    private Resource<Ecoute> ecouteToResource(Ecoute e, Boolean collection){
        Link selfLink = linkTo(EcouteRepresentation.class)
                .slash(e.getId_ecoute())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(EcouteRepresentation.class).getAllEcoutes())
                    .withRel("collection");
            return new Resource<>(e, selfLink, collectionLink);
        } else {
            return new Resource<>(e, selfLink);
        }
    }
}