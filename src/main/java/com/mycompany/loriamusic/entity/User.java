package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * User class
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Entity
@Table(name = "User")
public class User implements Serializable {

    @Id
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String first_name;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String last_name;
    
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_algo", nullable = false)
    private Algorithm algorithm;

    @OneToMany(mappedBy="user")
    @JsonIgnore
    private Set<SessionUser> sessions;
    
    @OneToMany(mappedBy="user")
    @JsonIgnore
    private Set<Liked> likes;
    
    @OneToMany(mappedBy="user")
    @JsonIgnore
    private Set<Baned> bans;
   
    // algo
    
    public User() {
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String nom) {
        this.last_name = nom;
    }

    public String getfFirst_name() {
        return first_name;
    }

    public void setFirst_name(String prenom) {
        this.first_name = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String mdp) {
        this.password = mdp;
    }

    public Set<SessionUser> getSessions() {
        return sessions;
    }

    public void setSessions(Set<SessionUser> sessions) {
        this.sessions = sessions;
    }

    public Set<Liked> getLikes() {
        return likes;
    }

    public void setLikes(Set<Liked> likes) {
        this.likes = likes;
    }

    public Set<Baned> getBans() {
        return bans;
    }

    public void setBans(Set<Baned> bans) {
        this.bans = bans;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
