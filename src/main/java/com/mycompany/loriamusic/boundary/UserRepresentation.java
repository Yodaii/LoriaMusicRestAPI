package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.User;
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
@RequestMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(User.class)
public class UserRepresentation {
    @Autowired
    UserResource ur;
    
    //GET
    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        Iterable<User> allUsers = ur.findAll();
        return new ResponseEntity<>(userToResource(allUsers), HttpStatus.OK);
    }
    
     //GET une instance
    @GetMapping(value="/{userid}")
    public ResponseEntity<?> getOneUser(@PathVariable("userid") Long id){
        return Optional.ofNullable(ur.findOne(id))
                .map(found -> new ResponseEntity(userToResource(found,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     //UPDATE PUT
    @PutMapping(value="/{userid}")
    public ResponseEntity<?> updateUser(@RequestBody User u, @PathVariable("userid") Long id){
        u.setId_user(id);
        User user = ur.save(u);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //POST
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User u){
        User saved = ur.save(u);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(UserRepresentation.class)
                .slash(saved.getId_user())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    private Resources<Resource<User>> userToResource(Iterable<User> all){
        Link selfLink = linkTo(methodOn(UserRepresentation.class).getAllUsers())
                .withSelfRel();
        List<Resource<User>> users = new ArrayList();
        all.forEach(user -> users.add(userToResource(user, false)));
        return new Resources<>(users,selfLink);
    }
    
    private Resource<User> userToResource(User u, Boolean collection){
        Link selfLink = linkTo(UserRepresentation.class)
                .slash(u.getId_user())
                .withSelfRel();
        if(collection){
            Link collectionLink = linkTo(methodOn(UserRepresentation.class).getAllUsers())
                    .withRel("collection");
            return new Resource<>(u, selfLink, collectionLink);
        } else {
            return new Resource<>(u, selfLink);
        }
    }
}
