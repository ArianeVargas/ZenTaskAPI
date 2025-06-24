package com.example.zentaskapi.repository;

import com.example.zentaskapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Buscar por username
    Optional<User> findByUsername(String username);

    // Buscar por email
    Optional<User> findByEmail(String email);

    // Verificar si existe un usuario por username
    boolean existsByUsername(String username);

    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);
}
