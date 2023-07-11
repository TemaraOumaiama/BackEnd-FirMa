package com.app.controller;

import com.app.modele.Departement;
import com.app.service.DepartementService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Departements")

public class DepartementController {
    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Departement>> getAllDepartements () {
        List<Departement> departements = departementService.findAllDepartements();
        return new ResponseEntity<>(departements, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Departement> getDepartementById (@PathVariable("id") Long id) {
        Departement departement;
        departement = departementService.findById(id);
        return new ResponseEntity<>(departement, HttpStatus.OK);
    }
    @GetMapping("/findname/{nom}")
    public ResponseEntity<Departement> getDepartementByNom(@PathVariable("nom") String id) {
        Departement user = departementService.findByNom(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @PostMapping("/add")
    public ResponseEntity<Departement> addDepartement(@RequestBody Departement departement) {

        Departement newDepartement = departementService.addDepartemnt(departement);

        return new ResponseEntity<>(newDepartement, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Departement> updateDepartement(@RequestBody Departement departement) {
        Departement updateDepartement = departementService.updateDepartement(departement);
        return new ResponseEntity<>(updateDepartement, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartement(@PathVariable("id") Long id) {
        departementService.deleteDepartementById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}