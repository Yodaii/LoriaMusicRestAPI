package com.mycompany.loriamusic.entity;

import java.util.ArrayList;
import java.util.List;
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
@Table(name = "Ecoute")
public class Ecoute {

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
    private Session session;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_track", nullable = false)
    private Track track;

    public Ecoute() {
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public List<Recommandation> calculRecommandation(String idUser, List<Track> tracks) {
        List<Recommandation> recos = new ArrayList<>();
        int compteur = 0;
        while(recos.size() < 9 && recos.size() != tracks.size()){
            Recommandation r = new Recommandation();
            r.setEcoute(this);
            r.setTrack(tracks.get(compteur));
            r.setEstChoisit(false);
            recos.add(r);
            compteur ++;
        }
        return recos;
    }
}
