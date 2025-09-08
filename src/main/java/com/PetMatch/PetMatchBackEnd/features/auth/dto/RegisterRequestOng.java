package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import lombok.Data;

@Data
public class RegisterRequestOng {
    private String enderecoOng;
    private String telefoneOng;
    private String celularOng;
    private String cnpjOng;
    private String emailOng;
    private String senhaOng;
}
