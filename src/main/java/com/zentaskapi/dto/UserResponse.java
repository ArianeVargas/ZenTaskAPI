package com.zentaskapi.dto;

import com.zentaskapi.entity.taskmanagerapi.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
}
