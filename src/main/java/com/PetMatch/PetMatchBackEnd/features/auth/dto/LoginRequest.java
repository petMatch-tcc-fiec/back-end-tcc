package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "LoginRequest",
        description = "Objeto de requisição usado para autenticação de usuários no sistema PetMatch."
)
public class LoginRequest {

    @Schema(description = "Endereço de e-mail do usuário cadastrado.", example = "usuario@exemplo.com")
    private String email;

    @Schema(description = "Senha associada à conta do usuário.", example = "Senha123!")
    private String password;
}
