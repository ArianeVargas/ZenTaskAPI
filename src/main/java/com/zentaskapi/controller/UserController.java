package com.zentaskapi.controller;

import com.zentaskapi.dto.CreateUserRequest;
import com.zentaskapi.dto.UpdateUserRequest;
import com.zentaskapi.dto.UserResponse;
import com.zentaskapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> partialUpdateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        String currentUsername = getCurrentUsername();
        boolean isAdmin = isCurrentUserAdmin();
        return ResponseEntity.ok(userService.partialUpdateUser(id, request, currentUsername, isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        String currentUsername = getCurrentUsername();
        boolean isAdmin = isCurrentUserAdmin();
        userService.deleteUser(id, currentUsername, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isCurrentUserAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
