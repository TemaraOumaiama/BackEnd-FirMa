package com.app.modele;

import com.app.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
@Getter
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id",nullable = false, updatable = false)
    private Long userId;
    private String username;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String nom;
    private String prenom;
    private String email;
    private String usertype;
    private String password;

    private Date datelastactivity;

    private Date lastLoginDateDisplay;


    private String phone;
    private String imageUrl;

    private String role;
    private String[] authorities;

    @ManyToOne
    private Societe societe;


    private boolean isActive;
    private  boolean isNotLocked;

    public User(Long id, String prenom, String nom, String username, String password, String email, String profileImageUrl, Date lastLoginDate, Date lastLoginDateDisplay, Date joinDate, String role, String[] authorities, boolean isActive, boolean isNotLocked) {
        this.userId = id;
        this.prenom = prenom;
        this.nom = nom;
        this.username = nom.toLowerCase()+prenom.toLowerCase();
        this.password = password;
        this.email = email;
        this.imageUrl = profileImageUrl;
        this.datelastactivity = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.datecreation = joinDate;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
    }


    @Column( updatable = false)
    private Date datecreation;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;


    public List<Document> getDocumentsModified() {
        return documentsModified;
    }

    public void setDocumentsModified(List<Document> documentsModified) {
        this.documentsModified = documentsModified;
    }

    public List<Document> getDocumentsCreated() {
        return documentsCreated;
    }

    public void setDocumentsCreated(List<Document> documentsCreated) {
        this.documentsCreated = documentsCreated;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "modifiedBy")
    private List<Document> documentsModified;


    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<Document> documentsCreated;





    public User(String nom, String prenom, String email, String usertype, String password, String phone, Departement departement) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.usertype = usertype;
        this.password = password;
        this.phone = phone;
        this.departement = departement;

    }

    public Departement getdepartement() {
        return departement;
    }

    public void setdepartement(Departement departement) {
        this.departement = departement;
    }

   public void setPassword(String password) {
        this.password = password;
    }



    public User() {

    }


    @Override
    public String toString() {
        return "User{" +
                "UserId=" + userId +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", usertype='" + usertype + '\'' +
                ", datecreation=" + datecreation +
                ", datelastactivity=" + datelastactivity +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }





    public void setUsername(String username) {
        this.username = username;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Date getDatelastactivity() {
        return datelastactivity;
    }

    public void setDatelastactivity(Date datelastactivity) {
        this.datelastactivity = datelastactivity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Date getLastLoginDateDisplay() {
        return lastLoginDateDisplay;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String roles) {
        this.role = roles;
    }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }



    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }


}
