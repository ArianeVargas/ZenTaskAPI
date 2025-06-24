package com.example.zentaskapi.service;

import com.example.zentaskapi.auth.dto.RegisterRequest;
import com.example.zentaskapi.dto.CreateUserRequest;
import com.example.zentaskapi.dto.UpdateUserRequest;
import com.example.zentaskapi.dto.UserResponse;
import com.example.zentaskapi.entity.User;
import com.example.zentaskapi.entity.taskmanagerapi.UserRole;
import com.example.zentaskapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<UserResponse> findById(UUID id) {
        return userRepository.findById(id).map(this::toResponse);
    }

    public UserResponse createUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername()) || userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Username or email already in use");
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
    public Optional<UserResponse> updateUser(UUID id, UpdateUserRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setRole(request.getRole());
            return toResponse(userRepository.save(user));
        });
    }

    @Transactional
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
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
