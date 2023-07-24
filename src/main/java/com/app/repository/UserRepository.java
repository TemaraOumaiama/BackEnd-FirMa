package com.app.repository;

import com.app.modele.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {


  //void deleteByUser_id(Long user_id);
  void deleteUserByUserId(Long id);
  List<User> findAllByOrderByUsernameAsc();

 User findByUserId(Long user_id);

User findByNom(String nom);

  User findUserByEmail(String email);

  User findByUsername(String nom);
  Page<User> findByNameContaining(String name, Pageable pageable);


}
