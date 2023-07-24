package com.app.modele;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "contrat")
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String Metadonnes;
    private Date dateDebut;
    private Date  dateEchance;

    private String Valider;// Contrat valider non valider et modifier

    private String Statu;// en cours / non valider/ signer (user)

    private boolean SignerFournisseur=false;
    private boolean SignerClient=false;
    private String  SignalerProblem;
    public String getValider() {
        return Valider;
    }

    public void setValider(String valider) {
        Valider = valider;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public boolean isSignerFournisseur() {
        return SignerFournisseur;
    }

    public void setSignerFournisseur(boolean signerFournisseur) {
        SignerFournisseur = signerFournisseur;
    }

    public boolean isSignerClient() {
        return SignerClient;
    }

    public void setSignerClient(boolean signerClient) {
        SignerClient = signerClient;
    }

    public String getSignalerProblem() {
        return SignalerProblem;
    }

    public void setSignalerProblem(String signalerProblem) {
        SignalerProblem = signalerProblem;
    }

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

    public Contrat(Long id, String nom, String metadonnes, Date dateDebut, Date dateEchance, String partieContractante, String fournisseur, User createdBy, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;
       this. Metadonnes = metadonnes;
        this.dateDebut = dateDebut;
        this.dateEchance = dateEchance;
        this.PartieContractante = partieContractante;
        this.fournisseur = fournisseur;
        this.createdBy = createdBy;
        this.departement = departement;
        this.dateCreation = new Date();

        this.dateModification = this.dateCreation;
        this.content = content;
    }

    public Contrat(Long id, String nom, Date dateDebut, Date dateEchance, String partieContractante, String fournisseur, User createdBy, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;

        this.dateDebut = dateDebut;
        this.dateEchance = dateEchance;
        this.PartieContractante = partieContractante;
        this.fournisseur = fournisseur;
        this.createdBy = createdBy;
        this.departement = departement;
        this.dateCreation = new Date();
        this.dateModification = this.dateCreation;
        this.content = content;
    }

    private String PartieContractante;
    private String fournisseur;
    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateEchance() {
        return dateEchance;
    }

    public void setDateEchance(Date dateEchance) {
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
        this.dateModification=new Date();
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
        this.dateModification = new Date();
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
        this.dateModification = new Date();
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
        this.dateModification = new Date();
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long modifiedById) {
        this.modifiedById = modifiedById;
        this.dateModification = new Date();
    }



    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }


    @Column( updatable = false)
    private Date dateCreation;

    private Date dateModification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.dateModification=new Date();

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.dateModification=new Date();

    }

    public String getMetadonnes() {
        return Metadonnes;
    }

    public void setMetadonnes(String metadonnes) {
        Metadonnes = metadonnes;
        this.dateModification=new Date();

    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        this.dateModification=new Date();

    }

    public User getModifiedBy() {
        return modifiedBy;

    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        this.dateModification=new Date();

    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
        this.dateModification=new Date();
    }

    public int getDelaiPreavis() {
        return delaiPreavis;
    }

    public void setDelaiPreavis(int delaiPreavis) {
        this.delaiPreavis = delaiPreavis;
    }

    public int delaiPreavis;

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
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
        this.dateModification=new Date();
        this.dateCreation=new Date();

        this.createdById=41L;



    }

    public Contrat(Long id, String nom, String metadonnes, Date dateDebut, Date dateEchance, User createdBy, Categorie categorie, Departement departement, byte[] content) {
        this.id = id;
        this.nom = nom;
      this.Metadonnes = metadonnes;
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