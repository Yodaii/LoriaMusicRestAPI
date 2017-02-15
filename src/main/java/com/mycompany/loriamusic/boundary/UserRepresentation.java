package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
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

/**
 * Rest controller for calls on user URL 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@RestController
@RequestMapping(value="/user", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(User.class)
public class UserRepresentation {
    @Autowired
    UserDAO userDao;
    
    /**
     * Method wich insert a new user in the database
     * @param u: user to insert
     * @return CREATED
     */
    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User u){
        String passwordHash = DigestUtils.sha1Hex(u.getPassword());
        u.setPassword(passwordHash);
        User saved = userDao.create(u);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(UserRepresentation.class)
                .slash(saved.getEmail())
                .toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
}
