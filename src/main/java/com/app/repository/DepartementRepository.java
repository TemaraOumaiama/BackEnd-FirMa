package com.app.repository;

import com.app.modele.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {

  //void deleteByUser_id(Long user_id);
  void deleteDepartementById(Long id);

  Optional<Departement> findById(Long id);
  Optional<Departement> findByNom(String nom);


}
