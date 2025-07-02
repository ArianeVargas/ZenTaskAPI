package com.zentaskapi.dto.user;

import com.zentaskapi.entity.taskmanagerapi.UserRole;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserRequest {

    private String fullName;
    @Email
    private String email;
    UserRole role;
}
