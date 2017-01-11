package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Algorithme;
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
@RequestMapping(value="/algo", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Algorithme.class)
public class AlgorithmeRepresentation { 
    @Autowired
    AlgorithmeResource ar;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllAlgorithmes(){
        Iterable<Algorithme> allAlgorithmes = ar.findAll();
        return new ResponseEntity<>(algorithmeToResource(allAlgorithmes), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{algoid}")
    public ResponseEntity<?> getOneAlgorithme(@PathVariable("algoid") Long id){
        return Optional.ofNullable(ar.findOne(id))
                .map(found -> new ResponseEntity(algorithmeToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{algoid}")
    public ResponseEntity<?> updateAlgorithme(@RequestBody Algorithme a, @PathVariable("algoid") Long id){
        a.setId_algo(id);
        Algorithme algo = ar.save(a);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveAlgorithme(@RequestBody Algorithme a){
        Algorithme saved = ar.save(a);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(AlgorithmeRepresentation.class)
                .slash(saved.getId_algo())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Algorithme>> algorithmeToResource(Iterable<Algorithme> all){
        Link selfLink = linkTo(methodOn(AlgorithmeRepresentation.class).getAllAlgorithmes())
                .withSelfRel();
        List<Resource<Algorithme>> algos = new ArrayList();
        all.forEach(algo -> algos.add(algorithmeToResource(algo, false)));
        return new Resources<>(algos,selfLink);
    }
    
    private Resource<Algorithme> algorithmeToResource(Algorithme a, Boolean collection){
        Link selfLink = linkTo(AlgorithmeRepresentation.class)
                .slash(a.getId_algo())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(AlgorithmeRepresentation.class).getAllAlgorithmes())
                    .withRel("collection");
            return new Resource<>(a, selfLink, collectionLink);
        } else {
            return new Resource<>(a, selfLink);
        }
    }
}