package com.zentaskapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String fullName;
    @NotBlank @Size(min = 6)
    String password;
}
