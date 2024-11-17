package com.monentreprise.gestion_employes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monentreprise.gestion_employes.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // Pour la recherche d'utilisateur par username
}
