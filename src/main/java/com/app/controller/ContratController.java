package com.app.controller;

import com.app.exception.*;
import com.app.modele.Contrat;
import com.app.modele.User;
import com.app.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@MultipartConfig

@RestController
@RequestMapping("/contrats")

public class ContratController   extends ExceptionHandling {

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

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
    @GetMapping("/all")
    public ResponseEntity<List<Contrat>> getAllContrats() {
        List<Contrat> Contrats = ContratService.findAllUsersSortedByNom();
        return new ResponseEntity<>(Contrats, HttpStatus.OK);
    }



    @PostMapping("/ajouter")
    public ResponseEntity<Contrat> addNewContrat(//@RequestParam("createdByUserId") Long createdByUserId,
                                           @RequestParam("nom") String type,
                                              @RequestParam("client") String client,
                                              @RequestParam("fournisseur") String fournisseur,
                                              @RequestParam("categorieId") Long categorieId,
                                           @RequestParam("departementId") Long departementId,
                                           @RequestParam("dateStart") Date dateStart,
                                           @RequestParam("dateEchance") Date dateEchance,
                                              @RequestParam("file") MultipartFile file) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException, URISyntaxException {
    Contrat newContrat=ContratService.addContrat(file,2L,categorieId,departementId,dateStart,dateEchance,client,fournisseur,type);
        return new ResponseEntity<>(newContrat, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Contrat> addUser(@RequestBody Contrat user, @RequestParam("file") MultipartFile file) throws IOException {

        Contrat newUser = ContratService.addContrat2(user,file);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

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








