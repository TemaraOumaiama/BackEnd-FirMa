package com.app.controller;

import com.app.exception.ExceptionHandling;
import com.app.modele.Document;
import com.app.modele.User;
import com.app.service.*;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/documents")

public class DocumentController  extends ExceptionHandling {

    private final DocumentService documentService;
    private final CategorieService categorieService;

    private final UserService userService;
    private final DepartementService departementService;
    private final MailService mailService;

    private final PDFService pdfService;
    public DocumentController(  PDFService pdfService ,DocumentService documentService, CategorieService categorieService,UserService userService,DepartementService departementService, MailService mailService) {
        this.documentService = documentService;
        this.categorieService=categorieService;
        this.userService=userService;
        this.departementService=departementService;
        this.mailService=mailService;
        this.pdfService=pdfService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAllUsersSortedByNom();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }




    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            documentService.uploadDocument(file);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }
    }



    @GetMapping("/created-by/{userId}")
    public List<Document> getDocumentsCreatedByUser(@PathVariable("userId") Long userId) {
        User user = userService.findByUserId((userId));
        if (user == null) {
            return null;
        }

        List<Document> documents = documentService.findAllUserCreated(user);
        return documents;
    }


    @GetMapping("/fetch-unread")
    public String fetchUnreadDocuments() {
        mailService.fetchUnreadDocuments();
        return "Unread documents fetched and saved successfully.";
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable("id") Long documentId) {
        Document document = documentService.findById(documentId);

        if (document != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(document.getNom())
                    .build());

            return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> addDocument(@RequestParam("file") MultipartFile file,
                                              @RequestParam("categorieId") Long categorieId,
                                              @RequestParam("departementId") Long departementId) {
        try {
            // Appel du service pour ajouter le document avec les informations fournies
            documentService.addDocument(file, categorieId, departementId);

            return ResponseEntity.ok("Document ajouté avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du document !");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<Document> updateDocument(@RequestParam(value = "file", required = false)
                                                       MultipartFile file, @ModelAttribute  Document updatedDocument) throws IOException {
        Document updated = documentService.updateDocument(updatedDocument, file);
        return ResponseEntity.ok(updated);
    }




    @GetMapping("/findName/{nom}")
    public ResponseEntity<Document> getDocumentByNom(@PathVariable("nom") String nom) {
        Document document = documentService.findByNom(nom);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }





    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocumentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



@PostMapping("/generatepdf")
public void generateSearchablePDF(@RequestParam("file") MultipartFile file) {
    try {
        pdfService.generateSearchablePDF(file);
    } catch (IOException e) {
        // Handle the exception appropriately
    }
}




}








