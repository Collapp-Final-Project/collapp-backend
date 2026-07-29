package com.collapp.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collapp.project.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

}
