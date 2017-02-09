package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Track")
public class Track implements Serializable {

    @Id
    @Column(name = "id_track")
    private String id_track;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "acousticness", nullable = true)
    private Double acousticness;

    @Column(name = "danceability", nullable = true)
    private Double danceability;

    @Column(name = "duration_ms", nullable = true)
    private Integer duration_ms;

    @Column(name = "energy", nullable = true)
    private Double energy;

    @Column(name = "instrumentalness", nullable = true)
    private Double instrumentalness;

    @Column(name = "key_track", nullable = true)
    private Integer key;

    @Column(name = "liveness", nullable = true)
    private Double liveness;

    @Column(name = "loudness", nullable = true)
    private Double loudness;

    @Column(name = "mode", nullable = true)
    private Integer mode;

    @Column(name = "speechiness", nullable = true)
    private Double speechiness;

    @Column(name = "tempo", nullable = true)
    private Double tempo;

    @Column(name = "time_signature", nullable = true)
    private Integer time_signature;

    @Column(name = "valence", nullable = true)
    private Double valence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name_artist", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy="track")
    @JsonIgnore
    private Set<Listening> listenings;
    
    @OneToMany(mappedBy="track")
    @JsonIgnore
    private Set<Recommendation> recommendations;
    
    @OneToMany(mappedBy="track")
    @JsonIgnore
    private Set<Like> likes;
    
    @OneToMany(mappedBy="track")
    @JsonIgnore
    private Set<Ban> bans;
    

    public Track() {
    }

    public String getId_track() {
        return id_track;
    }

    public void setId_track(String id_track) {
        this.id_track = id_track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titre) {
        this.title = titre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Double getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(Double acousticness) {
        this.acousticness = acousticness;
    }

    public Double getDanceability() {
        return danceability;
    }

    public void setDanceability(Double danceability) {
        this.danceability = danceability;
    }

    public Integer getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(Integer duration_ms) {
        this.duration_ms = duration_ms;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(Double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Double getLiveness() {
        return liveness;
    }

    public void setLiveness(Double liveness) {
        this.liveness = liveness;
    }

    public Double getLoudness() {
        return loudness;
    }

    public void setLoudness(Double loudness) {
        this.loudness = loudness;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(Double speechiness) {
        this.speechiness = speechiness;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Integer getTime_signature() {
        return time_signature;
    }

    public void setTime_signature(Integer time_signature) {
        this.time_signature = time_signature;
    }

    public Double getValence() {
        return valence;
    }

    public void setValence(Double valence) {
        this.valence = valence;
    }

    public Set<Listening> getListenings() {
        return listenings;
    }

    public void setListenings(Set<Listening> listenings) {
        this.listenings = listenings;
    }

    public Set<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Ban> getBans() {
        return bans;
    }

    public void setBans(Set<Ban> bans) {
        this.bans = bans;
    }
    
    
}
