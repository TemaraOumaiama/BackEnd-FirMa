package com.app.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="departement")
public class Departement implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "departement")
    private List<User> users;


    @JsonIgnore
    @OneToMany(mappedBy = "departement")
    private List<Document> documents;


    public Departement() {

    }

    public Departement(String id) {
        this.id = Long.parseLong(id);
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departement(Long id, String nom, String description, List<User> users) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.users = users;
    }

    public Departement(String nom, String description, List<User> users) {
        this.nom = nom;
        this.description = description;
        this.users = users;
    }

    public Departement(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public Departement( String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

}
