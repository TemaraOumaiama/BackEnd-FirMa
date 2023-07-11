package com.app.repository;

import com.app.modele.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

  void deleteCategorieById(Long id);

  Optional<Categorie> findById(Long id);
  Optional<Categorie> findByNom(String nom);


}
