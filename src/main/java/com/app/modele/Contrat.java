package com.app.modele;


import javax.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Table (name = "contrat")
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String Metadonnes;
    private LocalDateTime dateDebut;
    private LocalDateTime  dateEchance;

    public String getPartieContractante() {
        return PartieContractante;
    }

    public void setPartieContractante(String partieContractante) {
        PartieContractante = partieContractante;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Contrat(Long id, String nom, String metadonnes, LocalDateTime dateDebut, LocalDateTime dateEchance, String partieContractante, String fournisseur, User createdBy, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;
        Metadonnes = metadonnes;
        this.dateDebut = dateDebut;
        this.dateEchance = dateEchance;
        PartieContractante = partieContractante;
        this.fournisseur = fournisseur;
        this.createdBy = createdBy;
        this.departement = departement;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = this.dateCreation;
        this.content = content;
    }

    public Contrat(Long id, String nom, LocalDateTime dateDebut, LocalDateTime dateEchance, String partieContractante, String fournisseur, User createdBy, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;

        this.dateDebut = dateDebut;
        this.dateEchance = dateEchance;
        PartieContractante = partieContractante;
        this.fournisseur = fournisseur;
        this.createdBy = createdBy;
        this.departement = departement;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = this.dateCreation;
        this.content = content;
    }

    private String PartieContractante;
    private String fournisseur;
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateEchance() {
        return dateEchance;
    }

    public void setDateEchance(LocalDateTime dateEchance) {
        this.dateEchance = dateEchance;
    }


    @ManyToOne
    @JoinColumn(name = "created_By")
    private User createdBy;


    @ManyToOne
    @JoinColumn(name = "modified_By")
    private User modifiedBy;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.dateModification=LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "id_departement")
    private Departement departement;

    @Transient
    private MultipartFile file;
// Generate getter and setter for the 'file' field

    @Transient
    private Long departementId;

    public void setDepartementId(Long departementId) {
        this.departementId = departementId;
        this.dateModification = LocalDateTime.now();
    }

    @Transient
    private Long categorieId;

    @Transient
    private Long createdById;

    @Transient
    private Long modifiedById;

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
        this.dateModification = LocalDateTime.now();
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
        this.dateModification = LocalDateTime.now();
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long modifiedById) {
        this.modifiedById = modifiedById;
        this.dateModification = LocalDateTime.now();
    }



    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }


    @Column( updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.dateModification=LocalDateTime.now();

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.dateModification=LocalDateTime.now();

    }

    public String getMetadonnes() {
        return Metadonnes;
    }

    public void setMetadonnes(String metadonnes) {
        Metadonnes = metadonnes;
        this.dateModification=LocalDateTime.now();

    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        this.dateModification=LocalDateTime.now();

    }

    public User getModifiedBy() {
        return modifiedBy;

    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        this.dateModification=LocalDateTime.now();

    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
        this.dateModification=LocalDateTime.now();
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getDepartementId() {
        return departementId;
    }


    public Contrat(Long id, String nom, Categorie categorie, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.departement = departement;
        this.content = content;
        this.dateModification=LocalDateTime.now();
        this.dateCreation=LocalDateTime.now();

        this.createdById=2L;



    }

    public Contrat(Long id, String nom, String metadonnes, LocalDateTime dateDebut, LocalDateTime dateEchance, User createdBy, Categorie categorie, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;
        Metadonnes = metadonnes;
        this.dateDebut = dateDebut;
        this.dateEchance = dateEchance;
        this.createdBy = createdBy;
        this.categorie = categorie;
        this.departement = departement;
        this.content = content;
        this.modifiedBy=this.createdBy;
    }

    public Contrat() {



    }

    @Lob
    private byte[] content;

    // getters and setters
}