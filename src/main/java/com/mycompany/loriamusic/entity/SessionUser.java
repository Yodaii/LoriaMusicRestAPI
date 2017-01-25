package com.mycompany.loriamusic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "Sessionuser")
public class SessionUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_session")
    private long id_session;
    
    @Column(name = "date_debut", nullable = false)
    private Date dateDeb;
    
    @Column(name = "date_fin", nullable = true)
    private Date dateFin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy="session")
    @JsonIgnore
    private Set<Listening> listenings;
    
    public SessionUser() {
    }

    public long getId_session() {
        return id_session;
    }

    public void setId_session(long id_session) {
        this.id_session = id_session;
    }

    public Date getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(Date dateDeb) {
        this.dateDeb = dateDeb;
    }

    public Date getDateFinn() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Listening> getListenings() {
        return listenings;
    }

    public void setListenings(Set<Listening> listenings) {
        this.listenings = listenings;
    }
}
