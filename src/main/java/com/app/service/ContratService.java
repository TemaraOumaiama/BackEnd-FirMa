package com.app.service;

import com.app.modele.Contrat;
import com.app.modele.Departement;
import com.app.modele.User;
import com.app.repository.ContratRepository;

import com.app.exception.ContratNotFoundException;


import com.app.modele.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional

public class ContratService {


    private DepartementService departementService;
    ////////////////////////////////////////////////////////
    private com.app.repository.ContratRepository ContratRepository;
    ////////////////////////////////////////////////////
    private CategorieService categorieService;
    private UserService userService;






    @Autowired
    private MailService mailService;

    @Autowired
    public ContratService(ContratRepository ContratRepository, DepartementService departementService, CategorieService categorieService, UserService userService) {
        this.ContratRepository = ContratRepository;
        this.departementService=departementService;
        this.categorieService=categorieService;
        this.userService=userService;
    }



    public void addContrat(MultipartFile file,Long createdByUserId, Long categorieId, Long departementId) {
        try {
            // Vérifier si l'utilisateur existe
            User createdBy = userService.findByUserId(createdByUserId);
            if (createdBy == null) {
                throw new IllegalArgumentException("Utilisateur non trouvé");
            }

            // Vérifier si la catégorie existe
            Categorie categorie = categorieService.findById(categorieId);
            if (categorie == null) {
                throw new IllegalArgumentException("Catégorie non trouvée");
            }

            // Vérifier si le département existe
            Departement departement = departementService.findById(departementId);
            if (departement == null) {
                throw new IllegalArgumentException("Département non trouvé");
            }

            // Créer une instance de Contrat et définir les attributs
            Contrat Contrat = new Contrat();
            String originalFileName = file.getOriginalFilename();
            Contrat.setNom(originalFileName);
            Contrat.setContent(file.getBytes());
            Contrat.setCreatedBy(createdBy);
            Contrat.setModifiedBy(createdBy);
            Contrat.setCategorie(categorie);
            Contrat.setDepartement(departement);
            Contrat.setDateCreation(LocalDateTime.now());
            Contrat.setDateModification(LocalDateTime.now());

            // Vérifier si un fichier a été fourni
            if (!file.isEmpty()) {
                // Récupérer le contenu du fichier sous forme de tableau d'octets
                byte[] fileContent = file.getBytes();
                // Définir le contenu du Contrat avec le contenu du fichier
                Contrat.setContent(fileContent);
            }

            // Enregistrer le Contrat dans la base de données
            ContratRepository.save(Contrat);
            sendEmailNotification(Contrat);
        } catch (IOException e) {
            // Gérer l'exception en conséquence
            e.printStackTrace();
        }
    }







































    public List<Contrat> findAllContrats() {
        return ContratRepository.findAll();
    }

/*
    public Contrat saveContrat(MultipartFile file) throws IOException, IOException {
        String fileName = file.getOriginalFilename();
        byte[] content = file.getBytes();

        Contrat Contrat = new Contrat();
        Contrat.setNom(fileName);
        Contrat.setContent(content);

        return ContratRepository.save(Contrat);
    }
    */

    public void uploadContrat(MultipartFile file) {
        try {
            Contrat Contrat = new Contrat();
            Contrat.setNom(file.getOriginalFilename());
            Contrat.setContent(file.getBytes());
            Contrat.setDateCreation(LocalDateTime.now());
            Contrat.setDateModification(LocalDateTime.now());

            ContratRepository.save(Contrat);
        } catch (IOException e) {
            // Gérer les exceptions lors du traitement du fichier
        }





    }
// Find Contrat

    public Contrat findById(long id) {
        return ContratRepository.findById(id)
                .orElseThrow(() -> new ContratNotFoundException("Contrat introuvable avec l'identifiant : " + id));
    }

    public Contrat findByNom(String nom) {
        return ContratRepository.findByNom(nom)
                .orElseThrow(() -> new ContratNotFoundException("Departement introuvable avec l'identifiant : " + nom));
    }
    /*
        public void deleteUser(Long id){
            userRepository.deleteByUser_id(id);

        }
    */
    public void deleteContratById(Long id) {
        ContratRepository.deleteContratById(id);
    }




    public List<Contrat> findAllUsersSortedByNom() {
        return ContratRepository.findAllByOrderByNomAsc();
    }


    public List<Contrat> findAllUserCreated(User user) {
        return ContratRepository.findAllByCreatedByOrderByNomAsc(user);
    }








    public Contrat updateContrat(Contrat updatedContrat, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Optional<Contrat> optionalContrat = ContratRepository.findById(updatedContrat.getId());

        if (optionalContrat.isPresent()) {



            Contrat existingContrat = optionalContrat.get();

            if (file != null && !file.isEmpty()) {
                uploadContratContent(file, existingContrat);
            }

            if (updatedContrat.getNom() != null) {
                existingContrat.setNom(updatedContrat.getNom());
            }
            if (updatedContrat.getMetadonnes() != null) {
                existingContrat.setMetadonnes(updatedContrat.getMetadonnes());
            }
            // Creator User
            if (updatedContrat.getCreatedBy() != null) {
                User updatedCreatedBy = updatedContrat.getCreatedBy();
                User existingCreatedBy = existingContrat.getCreatedBy();

                if (!existingCreatedBy.getUserId().equals(updatedCreatedBy.getUserId())) {
                    User newCreatedBy = userService.findByUserId(updatedCreatedBy.getUserId());

                    if (newCreatedBy != null) {
                        existingContrat.setCreatedBy(newCreatedBy);
                    }
                }
            }

            if (updatedContrat.getModifiedBy() != null) {
                User updatedModifiedBy= updatedContrat.getModifiedBy();
                User existingModifiedBy= existingContrat.getModifiedBy();

                if (!existingModifiedBy.getUserId().equals(updatedModifiedBy.getUserId())) {
                    User newModifiedBy = userService.findByUserId(updatedModifiedBy.getUserId());

                    if (newModifiedBy != null) {
                        existingContrat.setCreatedBy(newModifiedBy);
                    }
                }
            }
            if (updatedContrat.getCategorie() != null) {
                Categorie updatedCategorie = updatedContrat.getCategorie();
                Categorie existingCategorie = existingContrat.getCategorie();

                if (!existingCategorie.getId().equals(updatedCategorie.getId())) {
                    Categorie newCategorie = categorieService.findById(updatedCategorie.getId());

                    existingContrat.setCategorie(newCategorie);
                }
            }

            if (updatedContrat.getDepartement() != null) {
                Departement updatedDepartement = updatedContrat.getDepartement();
                Departement existingDepartement = existingContrat.getDepartement();

                if (!existingDepartement.getId().equals(updatedDepartement.getId())) {
                    Departement newDepartement = departementService.findById(updatedDepartement.getId());
                    existingContrat.setDepartement(newDepartement);
                }
            }

            return ContratRepository.save(existingContrat);

        }

        else {
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("Contrat not found with ID: " + updatedContrat.getId());
        }


    }






    public void uploadContratContent(MultipartFile file, Contrat Contrat) throws IOException {
        byte[] content = file.getBytes();
        Contrat.setContent(content);
    }


    private void sendEmailNotification(Contrat Contrat) {
        // Préparation du contenu de l'e-mail
        String recipient = "testtechnicc@gmail.com";
        String subject = "Nouveau Contrat ajouté";
        String content = "Un nouveau Contrat a été ajouté : " + Contrat.getNom();

        // Envoi de l'e-mail
        mailService.sendEmail(recipient, subject, content);
    }












}













