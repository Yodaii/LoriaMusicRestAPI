package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.SessionUser;
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
@RequestMapping(value="/session", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(SessionUser.class)
public class SessionUserRepresentation { 
    @Autowired
    SessionUserDAO sessionUserDao;
    
    @Autowired
    UserDAO userDao;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllSessions(){
        Iterable<SessionUser> allSessions = sessionUserDao.getAll();
        return new ResponseEntity<>(sessionToResource(allSessions), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{sessionid}")
    public ResponseEntity<?> getOneSession(@PathVariable("sessionid") Long id){
        return Optional.ofNullable(sessionUserDao.getById(id))
                .map(found -> new ResponseEntity(sessionToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{idUser}")
    public ResponseEntity<?> updateSession(@PathVariable("idUser") String idUser){
        sessionUserDao.endSessionUser(userDao.getById(idUser));
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveSession(@RequestBody SessionUser s){
        SessionUser saved = sessionUserDao.create(s);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(SessionUserRepresentation.class)
                .slash(saved.getId_session())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<SessionUser>> sessionToResource(Iterable<SessionUser> all){
        Link selfLink = linkTo(methodOn(SessionUserRepresentation.class).getAllSessions())
                .withSelfRel();
        List<Resource<SessionUser>> sessions = new ArrayList();
        all.forEach(session -> sessions.add(sessionToResource(session, false)));
        return new Resources<>(sessions,selfLink);
    }
    
    private Resource<SessionUser> sessionToResource(SessionUser s, Boolean collection){
        Link selfLink = linkTo(SessionUserRepresentation.class)
                .slash(s.getId_session())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(SessionUserRepresentation.class).getAllSessions())
                    .withRel("collection");
            return new Resource<>(s, selfLink, collectionLink);
        } else {
            return new Resource<>(s, selfLink);
        }
    }
}