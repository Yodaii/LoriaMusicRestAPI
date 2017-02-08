package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

    @OneToMany(mappedBy="user")
    @JsonIgnore
    private Set<SessionUser> sessions;
    
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
}
