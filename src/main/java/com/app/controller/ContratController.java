package com.app.controller;

import com.app.modele.Contrat;
import com.app.modele.User;
import com.app.service.*;



import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/contrats")

public class ContratController {

    private final ContratService ContratService;
    private final CategorieService categorieService;

    private final UserService userService;
    private final DepartementService departementService;
    private final MailService mailService;

    private final PDFService pdfService;
    public ContratController(PDFService pdfService , ContratService ContratService, CategorieService categorieService, UserService userService, DepartementService departementService, MailService mailService) {
        this.ContratService = ContratService;
        this.categorieService=categorieService;
        this.userService=userService;
        this.departementService=departementService;
        this.mailService=mailService;
        this.pdfService=pdfService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Contrat>> getAllContrats() {
        List<Contrat> Contrats = ContratService.findAllUsersSortedByNom();
        return new ResponseEntity<>(Contrats, HttpStatus.OK);
    }




    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            ContratService.uploadContrat(file);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }
    }



    @GetMapping("/created-by/{userId}")
    public List<Contrat> getContratsCreatedByUser(@PathVariable("userId") Long userId) {
        User user = userService.findByUserId((userId));
        if (user == null) {
            return null;
        }

        List<Contrat> contrats = ContratService.findAllUserCreated(user);
        return contrats;
    }




    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewContrat(@PathVariable("id") Long ContratId) {
        Contrat Contrat = ContratService.findById(ContratId);

        if (Contrat != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(Contrat.getNom())
                    .build());

            return new ResponseEntity<>(Contrat.getContent(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> addContrat(@RequestParam("file") MultipartFile file,
                                             @RequestParam("categorieId") Long categorieId,
                                             @RequestParam("userId") Long userId,
                                             @RequestParam("departementId") Long departementId) {
        try {
            // Appel du service pour ajouter le Contrat avec les informations fournies
            ContratService.addContrat(file, categorieId, userId, departementId);

            return ResponseEntity.ok("Contrat ajouté avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du Contrat !");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<Contrat> updateContrat(@RequestParam(value = "file", required = false)
                                                 MultipartFile file, @ModelAttribute  Contrat updatedContrat) throws IOException {
        Contrat updated = ContratService.updateContrat(updatedContrat, file);
        return ResponseEntity.ok(updated);
    }




    @GetMapping("/findName/{nom}")
    public ResponseEntity<Contrat> getContratByNom(@PathVariable("nom") String nom) {
        Contrat Contrat = ContratService.findByNom(nom);
        return new ResponseEntity<>(Contrat, HttpStatus.OK);
    }





    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContrat(@PathVariable("id") Long id) {
        ContratService.deleteContratById(id);
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








