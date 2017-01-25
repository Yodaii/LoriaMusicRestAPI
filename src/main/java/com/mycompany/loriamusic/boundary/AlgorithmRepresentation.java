package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.AlgorithmDAO;
import com.mycompany.loriamusic.entity.Algorithm;
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
@RequestMapping(value="/algo", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Algorithm.class)
public class AlgorithmRepresentation { 
    @Autowired
    private AlgorithmDAO algoDao;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllAlgorithmes(){
        Iterable<Algorithm> allAlgorithmes = algoDao.getAll();
        return new ResponseEntity<>(algorithmeToResource(allAlgorithmes), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{algoid}")
    public ResponseEntity<?> getOneAlgorithme(@PathVariable("algoid") Long id){
        return Optional.ofNullable(algoDao.getById(id))
                .map(found -> new ResponseEntity(algorithmeToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{algoid}")
    public ResponseEntity<?> updateAlgorithme(@RequestBody Algorithm a, @PathVariable("algoid") Long id){
        a.setId_algo(id);
        Algorithm algo = algoDao.update(a);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveAlgorithme(@RequestBody Algorithm a){
        Algorithm saved = algoDao.create(a);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(AlgorithmRepresentation.class)
                .slash(saved.getId_algo())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Algorithm>> algorithmeToResource(Iterable<Algorithm> all){
        Link selfLink = linkTo(methodOn(AlgorithmRepresentation.class).getAllAlgorithmes())
                .withSelfRel();
        List<Resource<Algorithm>> algos = new ArrayList();
        all.forEach(algo -> algos.add(algorithmeToResource(algo, false)));
        return new Resources<>(algos,selfLink);
    }
    
    private Resource<Algorithm> algorithmeToResource(Algorithm a, Boolean collection){
        Link selfLink = linkTo(AlgorithmRepresentation.class)
                .slash(a.getId_algo())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(AlgorithmRepresentation.class).getAllAlgorithmes())
                    .withRel("collection");
            return new Resource<>(a, selfLink, collectionLink);
        } else {
            return new Resource<>(a, selfLink);
        }
    }
}