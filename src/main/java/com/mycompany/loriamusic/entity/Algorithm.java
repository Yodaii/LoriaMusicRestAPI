package com.mycompany.loriamusic.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Algorithm")
public class Algorithm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_algo")
    private long id_algo;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public Algorithm() {
    }

    public long getId_algo() {
        return id_algo;
    }

    public void setId_algo(long id_algo) {
        this.id_algo = id_algo;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
    }
}
