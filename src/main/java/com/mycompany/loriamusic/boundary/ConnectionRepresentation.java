package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.SessionUser;
import com.mycompany.loriamusic.entity.User;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping(value="/connection", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(User.class)
public class ConnectionRepresentation {
    @Autowired
    UserDAO userDao;
    
    @Autowired
    SessionUserDAO sessionUserDao;
    
    //POST
    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping
    public ResponseEntity<?> getUser(@RequestBody User u){
        User exist = userDao.getById(u.getEmail());
        HttpHeaders responseHeaders = new HttpHeaders();
        
        if(exist != null && exist.getPassword().equals(DigestUtils.sha1Hex(u.getPassword()))){
            SessionUser nvSession = new SessionUser();
            nvSession.setBegin_date(new Date());
            nvSession.setUser(exist);
            sessionUserDao.create(nvSession);
            responseHeaders.setLocation(linkTo(UserRepresentation.class)
                .slash(exist.getEmail())
                .toUri());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
        
    }
}
