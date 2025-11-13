package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
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

    // --- 2. Adicione os campos que o frontend precisa ---
    private UUID id;
    private String email;
    private String nome; // Baseado no seu RegisterRequest (campo 'name')
    private String tipo; // Baseado no seu AuthContext (Ex: "ONG" ou "ADOTANTE")
}
