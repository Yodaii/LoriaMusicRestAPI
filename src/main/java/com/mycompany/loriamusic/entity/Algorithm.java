package com.mycompany.loriamusic.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Algorithme")
public class Algorithm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_algo")
    private long id_algo;
    
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    public Algorithm() {
    }

    public long getId_algo() {
        return id_algo;
    }

    public void setId_algo(long id_algo) {
        this.id_algo = id_algo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
