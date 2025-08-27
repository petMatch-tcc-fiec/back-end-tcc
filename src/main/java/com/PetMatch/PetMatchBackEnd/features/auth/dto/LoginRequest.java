package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String senha;
}
