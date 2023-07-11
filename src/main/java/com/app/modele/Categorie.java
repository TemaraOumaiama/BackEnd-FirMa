package com.app.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="categorie")
public class Categorie implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;


    public Categorie() {

    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categorie(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;

    }

    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;

    }


    @JsonIgnore
    @OneToMany(mappedBy = "categorie")
    private List<Document> documents;


}
