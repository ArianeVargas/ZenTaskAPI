package com.zentaskapi.controller;

import com.zentaskapi.dto.user.CreateUserRequest;
import com.zentaskapi.dto.user.UpdateUserRequest;
import com.zentaskapi.dto.user.UserResponse;
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
        return ResponseEntity.ok(userService.findByUsername(getCurrentUsername()));
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
        return ResponseEntity.ok(userService.partialUpdateUser(id, request, getCurrentUsername(), isCurrentUserAdmin()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        String successMessage = userService.deleteUser(id, getCurrentUsername(), isCurrentUserAdmin());
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
