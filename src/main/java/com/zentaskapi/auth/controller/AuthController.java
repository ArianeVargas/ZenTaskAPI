package com.zentaskapi.auth.controller;

import com.zentaskapi.auth.dto.AuthRequest;
import com.zentaskapi.auth.dto.RegisterRequest;
import com.zentaskapi.auth.service.JwtUtil;
import com.zentaskapi.entity.User;
import com.zentaskapi.entity.taskmanagerapi.UserRole;
import com.zentaskapi.mapper.UserMapper;
import com.zentaskapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepo,
                          PasswordEncoder encoder, UserMapper userMapper) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        User user = userRepo.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername()) || userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario o correo electrónico ya en uso");
        }

        User user = userMapper.fromRegisterRequest(req);
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setRole(UserRole.USER);
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario creado exitosamente"));
    }
}