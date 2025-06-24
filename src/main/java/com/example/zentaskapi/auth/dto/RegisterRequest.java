package com.example.zentaskapi.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String fullName;
    private String password;
    private String email;
}
