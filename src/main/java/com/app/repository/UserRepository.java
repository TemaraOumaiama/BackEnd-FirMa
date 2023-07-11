package com.app.repository;

import com.app.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


  //void deleteByUser_id(Long user_id);
  void deleteUserByUserId(Long id);
  List<User> findAllByOrderByUsernameAsc();

 User findByUserId(Long user_id);

User findByNom(String nom);

  User findUserByEmail(String email);

  User findByUsername(String nom);


}
