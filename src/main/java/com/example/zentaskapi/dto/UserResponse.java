package com.example.zentaskapi.dto;

import com.example.zentaskapi.entity.taskmanagerapi.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
}
