package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Genre")
public class Genre implements Serializable {
    @Id
    @Column(name = "nom_genre", length = 100)
    private String nom;
    
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="genres")
    @JsonIgnore
    private Set<Artist> artists;  
    
    public Genre(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }
}
