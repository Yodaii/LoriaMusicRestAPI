package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Session;
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
@RequestMapping(value="/session", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Session.class)
public class SessionRepresentation { 
    @Autowired
    SessionResource sr;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllSessions(){
        Iterable<Session> allSessions = sr.findAll();
        return new ResponseEntity<>(sessionToResource(allSessions), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{sessionid}")
    public ResponseEntity<?> getOneSession(@PathVariable("sessionid") Long id){
        return Optional.ofNullable(sr.findOne(id))
                .map(found -> new ResponseEntity(sessionToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{sessionid}")
    public ResponseEntity<?> updateSession(@RequestBody Session s, @PathVariable("sessionid") Long id){
        s.setId_session(id);
        Session session = sr.save(s);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveSession(@RequestBody Session s){
        Session saved = sr.save(s);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(SessionRepresentation.class)
                .slash(saved.getId_session())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<Session>> sessionToResource(Iterable<Session> all){
        Link selfLink = linkTo(methodOn(SessionRepresentation.class).getAllSessions())
                .withSelfRel();
        List<Resource<Session>> sessions = new ArrayList();
        all.forEach(session -> sessions.add(sessionToResource(session, false)));
        return new Resources<>(sessions,selfLink);
    }
    
    private Resource<Session> sessionToResource(Session s, Boolean collection){
        Link selfLink = linkTo(SessionRepresentation.class)
                .slash(s.getId_session())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(SessionRepresentation.class).getAllSessions())
                    .withRel("collection");
            return new Resource<>(s, selfLink, collectionLink);
        } else {
            return new Resource<>(s, selfLink);
        }
    }
}
