package com.app.controller;

import com.app.exception.ExceptionHandling;
import com.app.modele.Categorie;
import com.app.service.CategorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Categories")

public class CategorieController extends ExceptionHandling {
    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Categorie>> getAllCategories () {
        List<Categorie> departements = categorieService.findAllCategories();
        return new ResponseEntity<>(departements, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Categorie> getCategorieById (@PathVariable("id") Long id) {
        Categorie categorie;
        categorie = categorieService.findById(id);
        return new ResponseEntity<>(categorie, HttpStatus.OK);
    }
    @GetMapping("/findname/{nom}")
    public ResponseEntity<Categorie> getCategorieByNom(@PathVariable("nom") String id) {
        Categorie categorie = categorieService.findByNom(id);
            return new ResponseEntity<>(categorie, HttpStatus.OK);
    }



    @PostMapping("/add")
    public ResponseEntity<Categorie> addCategorie(@RequestBody Categorie categorie) {

        Categorie newCategorie = categorieService.addCategorie(categorie);

        return new ResponseEntity<>(newCategorie, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Categorie> updateCategorie(@RequestBody Categorie categorie) {
        Categorie updateCategorie = categorieService.updateCategorie(categorie);
        return new ResponseEntity<>(updateCategorie, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategorie(@PathVariable("id") Long id) {
        categorieService.deleteCategorieById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}