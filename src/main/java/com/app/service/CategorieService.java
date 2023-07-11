package com.app.service;

import com.app.repository.CategorieRepository;
import com.app.modele.Categorie;
import com.app.exception.DepartementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional

public class CategorieService {


    private CategorieRepository categorieRepository;

    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }


    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public List<Categorie> findAllCategories() {
        return categorieRepository.findAll(Sort.by("nom"));
    }

    public Categorie updateCategorie(Categorie updatedCategorie) {
        Optional<Categorie> optionalCategorie= categorieRepository.findById(updatedCategorie.getId());
        if (optionalCategorie.isPresent()) {
            Categorie existingCategorie= optionalCategorie.get();

            // Update only the desired attribute(s)
            if (updatedCategorie.getNom() != null) {
                existingCategorie.setNom(updatedCategorie.getNom());
            }
            if (updatedCategorie.getDescription() != null) {
                existingCategorie.setDescription(updatedCategorie.getDescription());
            }


            // Save the updated entity
            return categorieRepository.save(existingCategorie);
        } else {
            // Handle the case where the user does not exist
            throw new IllegalArgumentException("Categorie not found with ID: " + updatedCategorie.getId());
        }
    }






    public Categorie findById(long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new DepartementNotFoundException("Categorie introuvable avec l'identifiant : " + id));
    }


    public Categorie findByNom(String nom) {
        return categorieRepository.findByNom(nom)
                .orElseThrow(() -> new DepartementNotFoundException("Categorie introuvable avec l'identifiant : " + nom));
    }

public void deleteCategorieById(Long id) {
    categorieRepository.deleteCategorieById(id);
}

}
