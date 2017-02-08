package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Listening")
public class Listening implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ecoute")
    private long id_ecoute;
    
    @Column(name = "aimer", nullable = true)
    private boolean aimer;
   
    @Column(name = "duree", nullable = true)
    private float duree;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    @JsonIgnore
    private SessionUser session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false)
    @JsonIgnore
    private Track track;
    
    @OneToMany(mappedBy="listening")
    @JsonIgnore
    private Set<Recommendation> recommendations;
    
    public Listening() {
    }

    public long getId_ecoute() {
        return id_ecoute;
    }

    public void setId_ecoute(long id_ecoute) {
        this.id_ecoute = id_ecoute;
    }

    public boolean isAimer() {
        return aimer;
    }

    public void setAimer(boolean aimer) {
        this.aimer = aimer;
    }

    public float getDuree() {
        return duree;
    }

    public void setDuree(float duree) {
        this.duree = duree;
    }

    public SessionUser getSession() {
        return session;
    }

    public void setSession(SessionUser session) {
        this.session = session;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Set<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
