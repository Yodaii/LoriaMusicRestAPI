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
    
    @Column(name = "begin_date", nullable = false)
    private Date begin_date;
    
    @Column(name = "end_date", nullable = true)
    private Date end_date;
    
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

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date dateDeb) {
        this.begin_date = dateDeb;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date dateFin) {
        this.end_date = dateFin;
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
