package com.app.service;

import com.app.repository.DepartementRepository;
import com.app.modele.Departement;
import com.app.exception.DepartementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional

public class DepartementService {


    private DepartementRepository departementRepository;

    @Autowired
    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    public Departement  addDepartemnt(Departement departement) {
        return departementRepository.save(departement);
    }

    public List<Departement> findAllDepartements() {
        return departementRepository.findAll();
    }

    public Departement updateDepartement(Departement updatedDepartement) {
        Optional<Departement> optionalDepartement= departementRepository.findById(updatedDepartement.getId());
        if (optionalDepartement.isPresent()) {
            Departement existingDepartement= optionalDepartement.get();

            // Update only the desired attribute(s)
            if (updatedDepartement.getNom() != null) {
                existingDepartement.setNom(updatedDepartement.getNom());
            }
            if (updatedDepartement.getDescription() != null) {
                existingDepartement.setDescription(updatedDepartement.getDescription());
            }

            if (updatedDepartement.getUsers() != null) {
                existingDepartement.setUsers(updatedDepartement.getUsers());
            }

            // Save the updated entity
            return departementRepository.save(existingDepartement);
        } else {
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("Departement not found with ID: " + updatedDepartement.getId());
        }
    }


    public Departement findById(long id) {
        return departementRepository.findById(id)
                .orElseThrow(() -> new DepartementNotFoundException("Departement introuvable avec l'identifiant : " + id));
    }


    public Departement findByNom(String nom) {
        return departementRepository.findByNom(nom)
                .orElseThrow(() -> new DepartementNotFoundException("Departement introuvable avec l'identifiant : " + nom));
    }
/*
    public void deleteUser(Long id){
        userRepository.deleteByUser_id(id);

    }
*/
public void deleteDepartementById(Long id) {
    departementRepository.deleteDepartementById(id);
}

}
