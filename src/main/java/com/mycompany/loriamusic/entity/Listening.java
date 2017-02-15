package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

/**
 * Listening class
 * @author Yohann Vaubourg & Arthur Flambeau
 */
@Entity
@Table(name = "Listening")
public class Listening implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_listening")
    private long id_listening;
    
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;
    
    @Column(name = "liked", nullable = true)
    private boolean liked;
   
    @Column(name = "duration", nullable = true)
    private float duration;
    
    @Column(name = "mode", nullable = false)
    private String mode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    @JsonIgnore
    private SessionUser session;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_track", nullable = false)
    private Track track;
    
    @OneToMany(mappedBy="listening")
    @JsonIgnore
    private Set<Recommendation> recommendations;
    
    public Listening() {
    }

    public long getId_listening() {
        return id_listening;
    }

    public void setId_listening(long id_ecoute) {
        this.id_listening = id_ecoute;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean aimer) {
        this.liked = aimer;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duree) {
        this.duration = duree;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
