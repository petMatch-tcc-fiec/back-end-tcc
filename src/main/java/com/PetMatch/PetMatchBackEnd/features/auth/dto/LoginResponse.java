package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "LoginResponse",
        description = "Resposta retornada após o login bem-sucedido, contendo o token JWT de autenticação."
)
public class LoginResponse {

    @Schema(
            description = "Token JWT gerado para autenticação do usuário.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;
}
