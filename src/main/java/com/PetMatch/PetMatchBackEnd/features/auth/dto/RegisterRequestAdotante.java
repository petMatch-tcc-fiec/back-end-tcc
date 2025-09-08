package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import lombok.Data;

@Data
public class RegisterRequestAdotante {
    private String nome;
    private String cpf;
    private String endereco;
    private String celular;
    private String email;
    private String senha;
    private String descricaoOutrosAnimais;
    private String preferencia;
    private UserLevel userLevel = UserLevel.USER;
}
