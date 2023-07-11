package com.app.controller;

import com.app.modele.Departement;
import com.app.modele.User;
import com.app.service.DepartementService;
import com.app.service.UserService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")

public class UserController {
    private final UserService userService;
    private final DepartementService departementService;

    public UserController(UserService userService, DepartementService departementService) {

        this.userService = userService;

        this.departementService = departementService;

    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers () {
        List<User> users = userService.findAllUsersSortedByUsername();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/findname/{nom}")
    public ResponseEntity<User> getUserByNom(@PathVariable("nom") String id) {
        User user = userService.findByNom(id);
        if (user != null) {
            Long departementId = user.getdepartement().getId();
            Departement departement = departementService.findById(departementId);
            user.setdepartement(departement);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.findByUserId(id);
        if (user != null) {
            Long departementId = user.getdepartement().getId();
            Departement departement = departementService.findById(departementId);
            user.setdepartement(departement);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {

            User newUser = userService.addUser(user);

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }




    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}