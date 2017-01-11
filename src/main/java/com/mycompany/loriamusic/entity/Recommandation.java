package com.mycompany.loriamusic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Recommandation")
public class Recommandation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reco")
    private long id_reco;
    
    @Column(name = "est_choisit", nullable = false)
    private boolean estChoisit;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false)
    private Track track;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ecoute", nullable = false)
    private Ecoute ecoute;

    public Recommandation() {
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

    public Ecoute getEcoute() {
        return ecoute;
    }

    public void setEcoute(Ecoute ecoute) {
        this.ecoute = ecoute;
    }
}
