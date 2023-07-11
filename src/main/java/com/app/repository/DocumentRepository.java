package com.app.repository;

import com.app.modele.Document;
import com.app.modele.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

  void deleteDocumentById(Long id);

  Optional<Document> findById(Long id);
  Optional<Document> findByNom(String nom);
  List<Document> findAllByOrderByNomAsc();

  List<Document> findAllByCreatedByOrderByNomAsc(User user);


}
