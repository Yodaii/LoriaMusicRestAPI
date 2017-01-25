package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Recommendation")
public class Recommendation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reco")
    private long id_reco;
    
    @Column(name = "est_choisit", nullable = false)
    private boolean estChoisit;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false)
    @JsonIgnore
    private Track track;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ecoute", nullable = false)
    @JsonIgnore
    private Listening ecoute;

    public Recommendation() {
    }

    public long getId_reco() {
        return id_reco;
    }

    public void setId_reco(long id_reco) {
        this.id_reco = id_reco;
    }

    public boolean isEstChoisit() {
        return estChoisit;
    }

    public void setEstChoisit(boolean estChoisit) {
        this.estChoisit = estChoisit;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Listening getEcoute() {
        return ecoute;
    }

    public void setEcoute(Listening ecoute) {
        this.ecoute = ecoute;
    }
}
