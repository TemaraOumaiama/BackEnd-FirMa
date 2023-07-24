package com.app.service;

import com.app.exception.DocumentNotFoundException;
import com.app.modele.Categorie;
import com.app.modele.Departement;
import com.app.modele.Document;
import com.app.modele.User;
import com.app.repository.DocumentRepository;



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

public class DocumentService {


    private DepartementService departementService;
    ////////////////////////////////////////////////////////
    private DocumentRepository documentRepository;
    ////////////////////////////////////////////////////
    private CategorieService categorieService;
    private UserService userService;






    @Autowired
    private MailService mailService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, DepartementService departementService, CategorieService categorieService, UserService userService) {
        this.documentRepository = documentRepository;
        this.departementService=departementService;
        this.categorieService=categorieService;
        this.userService=userService;
    }



    public void addDocument(MultipartFile file, Long categorieId, Long departementId) {
        try {
            // Vérifier si l'utilisateur existe
            User createdBy = userService.findByUserId(2L);
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

            // Créer une instance de Document et définir les attributs
            Document document = new Document();
            String originalFileName = file.getOriginalFilename();
            document.setNom(originalFileName);
            document.setContent(file.getBytes());
            document.setCreatedBy(createdBy);
            document.setModifiedBy(createdBy);
            document.setCategorie(categorie);
            document.setDepartement(departement);
            document.setDateCreation(LocalDateTime.now());
            document.setDateModification(LocalDateTime.now());

            // Vérifier si un fichier a été fourni
            if (!file.isEmpty()) {
                // Récupérer le contenu du fichier sous forme de tableau d'octets
                byte[] fileContent = file.getBytes();
                // Définir le contenu du document avec le contenu du fichier
                document.setContent(fileContent);
            }

            // Enregistrer le document dans la base de données
            documentRepository.save(document);
           // sendEmailNotification(document);
        } catch (IOException e) {
            // Gérer l'exception en conséquence
            e.printStackTrace();
        }
    }







































    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

/*
    public Document saveDocument(MultipartFile file) throws IOException, IOException {
        String fileName = file.getOriginalFilename();
        byte[] content = file.getBytes();

        Document document = new Document();
        document.setNom(fileName);
        document.setContent(content);

        return documentRepository.save(document);
    }
    */

    public void uploadDocument(MultipartFile file) {
        try {
            Document document = new Document();
            document.setNom(file.getOriginalFilename());
            document.setContent(file.getBytes());
            document.setDateCreation(LocalDateTime.now());
            document.setDateModification(LocalDateTime.now());

            documentRepository.save(document);
        } catch (IOException e) {
            // Gérer les exceptions lors du traitement du fichier
        }





    }
// Find Document

    public Document findById(long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document introuvable avec l'identifiant : " + id));
    }

    public Document findByNom(String nom) {
        return documentRepository.findByNom(nom)
                .orElseThrow(() -> new DocumentNotFoundException("Departement introuvable avec l'identifiant : " + nom));
    }
    /*
        public void deleteUser(Long id){
            userRepository.deleteByUser_id(id);

        }
    */
    public void deleteDocumentById(Long id) {
        documentRepository.deleteDocumentById(id);
    }




    public List<Document> findAllUsersSortedByNom() {
        return documentRepository.findAllByOrderByNomAsc();
    }


    public List<Document> findAllUserCreated(User user) {
        return documentRepository.findAllByCreatedByOrderByNomAsc(user);
    }








    public Document updateDocument(Document updatedDocument, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Optional<Document> optionalDocument = documentRepository.findById(updatedDocument.getId());

        if (optionalDocument.isPresent()) {



            Document existingDocument = optionalDocument.get();

            if (file != null && !file.isEmpty()) {
                uploadDocumentContent(file, existingDocument);
            }

            if (updatedDocument.getNom() != null) {
                existingDocument.setNom(updatedDocument.getNom());
            }
            if (updatedDocument.getMetadonnes() != null) {
                existingDocument.setMetadonnes(updatedDocument.getMetadonnes());
            }
            // Creator User
            if (updatedDocument.getCreatedBy() != null) {
                User updatedCreatedBy = updatedDocument.getCreatedBy();
                User existingCreatedBy = existingDocument.getCreatedBy();

                if (!existingCreatedBy.getUserId().equals(updatedCreatedBy.getUserId())) {
                    User newCreatedBy = userService.findByUserId(updatedCreatedBy.getUserId());

                    if (newCreatedBy != null) {
                        existingDocument.setCreatedBy(newCreatedBy);
                    }
                }
            }

            if (updatedDocument.getModifiedBy() != null) {
                User updatedModifiedBy= updatedDocument.getModifiedBy();
                User existingModifiedBy= existingDocument.getModifiedBy();

                if (!existingModifiedBy.getUserId().equals(updatedModifiedBy.getUserId())) {
                    User newModifiedBy = userService.findByUserId(2);

                    if (newModifiedBy != null) {
                        existingDocument.setCreatedBy(newModifiedBy);
                    }
                }
            }
            if (updatedDocument.getCategorie() != null) {
                Categorie updatedCategorie = updatedDocument.getCategorie();
                Categorie existingCategorie = existingDocument.getCategorie();

                if (!existingCategorie.getId().equals(updatedCategorie.getId())) {
                    Categorie newCategorie = categorieService.findById(updatedCategorie.getId());

                    existingDocument.setCategorie(newCategorie);
                }
            }

            if (updatedDocument.getDepartement() != null) {
                Departement updatedDepartement = updatedDocument.getDepartement();
                Departement existingDepartement = existingDocument.getDepartement();

                if (!existingDepartement.getId().equals(updatedDepartement.getId())) {
                    Departement newDepartement = departementService.findById(updatedDepartement.getId());
                    existingDocument.setDepartement(newDepartement);
                }
            }

            return documentRepository.save(existingDocument);

        }

        else {
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("Document not found with ID: " + updatedDocument.getId());
        }


    }






    public void uploadDocumentContent(MultipartFile file, Document document) throws IOException {
        byte[] content = file.getBytes();
        document.setContent(content);
    }


    private void sendEmailNotification(Document document) {
        // Préparation du contenu de l'e-mail
        String recipient = "testtechnicc@gmail.com";
        String subject = "Nouveau document ajouté";
        String content = "Un nouveau document a été ajouté : " + document.getNom();

        // Envoi de l'e-mail
        mailService.sendEmail(recipient, subject, content);
    }












}













