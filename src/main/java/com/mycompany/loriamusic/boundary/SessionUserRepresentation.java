package com.mycompany.loriamusic.boundary;

import com.mycompany.loriamusic.DAO.SessionUserDAO;
import com.mycompany.loriamusic.DAO.UserDAO;
import com.mycompany.loriamusic.entity.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for calls on session URL 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@RestController
@RequestMapping(value="/session", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(SessionUser.class)
public class SessionUserRepresentation { 
    @Autowired
    SessionUserDAO sessionUserDao;
    
    @Autowired
    UserDAO userDao;
    
    /**
     * Method wich close the current session of the selected user
     * @param idUser: user id of the session
     * @return NO CONTENT
     */
    @PutMapping(value="/{idUser}")
    public ResponseEntity<?> updateSession(@PathVariable("idUser") String idUser){
        sessionUserDao.endSessionUser(userDao.getById(idUser));
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}