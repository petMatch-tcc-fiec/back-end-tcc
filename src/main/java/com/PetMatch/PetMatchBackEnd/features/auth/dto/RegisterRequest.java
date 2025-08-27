package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private String picture;
}
