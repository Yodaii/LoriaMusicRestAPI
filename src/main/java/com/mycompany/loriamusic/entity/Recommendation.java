package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
@Table(name = "Recommendation")
public class Recommendation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_reco")
    private long id_reco;
    
    @Column(name = "is_choose", nullable = false)
    @JsonIgnore
    private boolean isChoose;
    
    @Column(name = "name_algorithm", nullable = false)
    private String nameAlgorithm;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_track", nullable = false)
    private Track track;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_listening", nullable = false)
    @JsonIgnore
    private Listening listening;

    public Recommendation() {
    }

    public long getId_reco() {
        return id_reco;
    }

    public void setId_reco(long id_reco) {
        this.id_reco = id_reco;
    }

    public boolean isIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean estChoisit) {
        this.isChoose = estChoisit;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Listening getListening() {
        return listening;
    }

    public void setListening(Listening listening) {
        this.listening = listening;
    }

    public String getNameAlgorithm() {
        return nameAlgorithm;
    }

    public void setNameAlgorithm(String nameAlgorithm) {
        this.nameAlgorithm = nameAlgorithm;
    }
}
