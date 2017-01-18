package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.entity.Session;
import com.mycompany.loriamusic.entity.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/connection", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(User.class)
public class ConnectionRepresentation {
    @Autowired
    UserResource cr;
    
    @Autowired
    SessionResource sr;
    
    //POST
    @PostMapping
    public ResponseEntity<?> getUser(@RequestBody User u){
        User exist = cr.findOne(u.getEmail());
        HttpHeaders responseHeaders = new HttpHeaders();
        
        if(exist != null && u.getMdp().equals(exist.getMdp())){
            Session nvSession = new Session();
            nvSession.setDateDeb(new Date());
            nvSession.setUser(exist);
            sr.save(nvSession);
            responseHeaders.setLocation(linkTo(UserRepresentation.class)
                .slash(exist.getEmail())
                .toUri());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
        
    }
}
