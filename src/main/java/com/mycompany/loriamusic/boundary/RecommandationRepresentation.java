package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Recommandation;
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
@RequestMapping(value="/reco", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Recommandation.class)
public class RecommandationRepresentation { 
    @Autowired
    RecommandationResource rr;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllRecommandations(){
        Iterable<Recommandation> allRecommandations = rr.findAll();
        return new ResponseEntity<>(recommandationToResource(allRecommandations), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{recoid}")
    public ResponseEntity<?> getOneRecommandation(@PathVariable("recoid") Long id){
        return Optional.ofNullable(rr.findOne(id))
                .map(found -> new ResponseEntity(recommandationToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{recoid}")
    public ResponseEntity<?> updateRecommandation(@RequestBody Recommandation r, @PathVariable("recoid") Long id){
        r.setId_reco(id);
        Recommandation recommandation = rr.save(r);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveRecommandation(@RequestBody Recommandation r){
        Recommandation saved = rr.save(r);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(RecommandationRepresentation.class)
                .slash(saved.getId_reco())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Recommandation>> recommandationToResource(Iterable<Recommandation> all){
        Link selfLink = linkTo(methodOn(RecommandationRepresentation.class).getAllRecommandations())
                .withSelfRel();
        List<Resource<Recommandation>> recommandations = new ArrayList();
        all.forEach(recommandation -> recommandations.add(recommandationToResource(recommandation, false)));
        return new Resources<>(recommandations,selfLink);
    }
    
    private Resource<Recommandation> recommandationToResource(Recommandation r, Boolean collection){
        Link selfLink = linkTo(RecommandationRepresentation.class)
                .slash(r.getId_reco())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(RecommandationRepresentation.class).getAllRecommandations())
                    .withRel("collection");
            return new Resource<>(r, selfLink, collectionLink);
        } else {
            return new Resource<>(r, selfLink);
        }
    }
}