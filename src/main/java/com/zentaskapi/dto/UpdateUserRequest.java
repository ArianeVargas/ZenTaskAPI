package com.zentaskapi.dto;

import com.zentaskapi.entity.taskmanagerapi.UserRole;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String fullName;

    @Email
    private String email;

    UserRole role;
}
