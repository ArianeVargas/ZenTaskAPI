package com.zentaskapi.service;

import com.zentaskapi.dto.CreateUserRequest;
import com.zentaskapi.dto.UpdateUserRequest;
import com.zentaskapi.dto.UserResponse;
import com.zentaskapi.entity.User;
import com.zentaskapi.entity.taskmanagerapi.UserRole;
import com.zentaskapi.exception.ResourceConflictException;
import com.zentaskapi.exception.ResourceNotFoundException;
import com.zentaskapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
    }

    public UserResponse createUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername()) || userRepository.existsByEmail(req.getEmail())) {
            throw new ResourceConflictException("El nombre de usuario o correo ya está en uso");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setFullName(req.getFullName());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setRole(UserRole.USER);

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse partialUpdateUser(UUID id, UpdateUserRequest request, String currentUsername, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Solo ADMIN o el mismo usuario pueden editar
        if (!isAdmin && !user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("No tienes permiso para editar este usuario");
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (isAdmin && request.getRole() != null) {
            user.setRole(request.getRole());
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID id, String currentUsername, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Solo ADMIN o el mismo usuario pueden eliminar
        if (!isAdmin && !user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("No tienes permiso para eliminar este usuario");
        }

        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }
}
