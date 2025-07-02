package com.zentaskapi.service;

import com.zentaskapi.dto.user.CreateUserRequest;
import com.zentaskapi.dto.user.UpdateUserRequest;
import com.zentaskapi.dto.user.UserResponse;
import com.zentaskapi.entity.User;
import com.zentaskapi.entity.taskmanagerapi.UserRole;
import com.zentaskapi.exception.ResourceConflictException;
import com.zentaskapi.exception.ResourceNotFoundException;
import com.zentaskapi.mapper.UserMapper;
import com.zentaskapi.repository.UserRepository;
import com.zentaskapi.security.PermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
    }

    public UserResponse createUser(CreateUserRequest req) {
        validateUniqueUsernameAndEmail(req.getUsername(), req.getEmail());
        User user = userMapper.fromCreateRequest(req);
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.USER);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponse partialUpdateUser(UUID id, UpdateUserRequest request, String currentUsername, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        permissionService.verifyUserPermission(user, currentUsername, isAdmin);
        userMapper.updateFromDto(request, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public String deleteUser(UUID id, String currentUsername, boolean isAdmin) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        permissionService.verifyUserPermission(user, currentUsername, isAdmin);
        userRepository.delete(user);
        return "Usuario eliminado exitosamente";
    }

    private void validateUniqueUsernameAndEmail(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("El correo electrónico ya está en uso");
        }
    }
}

