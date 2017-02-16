package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Algorithm class 
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Entity
@Table(name = "Algorithm")
public class Algorithm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_algo")
    private long id_algo;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy="algorithm")
    @JsonIgnore
    private Set<User> users;
    
    public Algorithm() {
    }

    public long getId_algo() {
        return id_algo;
    }

    public void setId_algo(long id_algo) {
        this.id_algo = id_algo;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
