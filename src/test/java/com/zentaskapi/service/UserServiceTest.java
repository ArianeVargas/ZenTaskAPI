package com.zentaskapi.service;

import com.zentaskapi.dto.user.CreateUserRequest;
import com.zentaskapi.dto.user.UserResponse;
import com.zentaskapi.entity.User;
import com.zentaskapi.entity.taskmanagerapi.UserRole;
import com.zentaskapi.mapper.UserMapper;
import com.zentaskapi.repository.UserRepository;
import com.zentaskapi.security.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .password("password123")
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        when(userMapper.fromCreateRequest(request)).thenAnswer(invocation -> {
            CreateUserRequest req = invocation.getArgument(0);
            return User.builder()
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .fullName(req.getFullName())
                    .build();
        });

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .passwordHash("encodedPassword")
                .role(UserRole.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .build());

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
    }
}
