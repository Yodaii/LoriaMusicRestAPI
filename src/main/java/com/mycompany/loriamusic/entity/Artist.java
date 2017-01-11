package com.mycompany.loriamusic.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Artist")
public class Artist {

    @Id
    @Column(name = "nom_artist", length = 100)
    private String nom;

    @Column(name = "popularity", nullable = true)
    private Integer popularity;
    
    @OneToMany(mappedBy="artist")
    private Set<Track> tracks;
    
    @ManyToMany(cascade=CascadeType.ALL)  
    @JoinTable(name="genres_artist", joinColumns=@JoinColumn(name="nom_artist"), inverseJoinColumns=@JoinColumn(name="nom_genre")) 
    private Set<Genre> genres;
    
    public Artist() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
