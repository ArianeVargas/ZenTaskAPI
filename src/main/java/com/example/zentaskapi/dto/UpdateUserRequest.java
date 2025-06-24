package com.example.zentaskapi.dto;

import com.example.zentaskapi.entity.taskmanagerapi.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
    UserRole role;
}
