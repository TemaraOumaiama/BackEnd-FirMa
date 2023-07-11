package com.app.repository;

import com.app.modele.Contrat;
import com.app.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

  void deleteContratById(Long id);

  Optional<Contrat> findById(Long id);
  Optional<Contrat> findByNom(String nom);
  List<Contrat> findAllByOrderByNomAsc();

  List<Contrat> findAllByCreatedByOrderByNomAsc(User user);


}
