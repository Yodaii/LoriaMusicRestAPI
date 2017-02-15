package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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

/**
 * Artist class
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Entity
@Table(name = "Artist")
public class Artist implements Serializable {

    @Id
    @Column(name = "name_artist", length = 100)
    private String name;

    @Column(name = "popularity", nullable = true)
    private Integer popularity;
    
    @OneToMany(mappedBy="artist")
    @JsonIgnore
    private Set<Track> tracks;
    
    @ManyToMany(cascade=CascadeType.ALL)  
    @JoinTable(name="genres_artist", joinColumns=@JoinColumn(name="nom_artist"), inverseJoinColumns=@JoinColumn(name="nom_genre")) 
    @JsonIgnore
    private Set<Genre> genres;
    
    public Artist() {
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
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
