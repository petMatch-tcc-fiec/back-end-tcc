package com.PetMatch.PetMatchBackEnd.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta retornada após a criação de um novo usuário.")
public class CreatedUsuarioResponseDto {

    @Schema(description = "Identificador único do registro criado no banco.", example = "8a8a8a8a-8a8a-8a8a-8a8a-8a8a8a8a8a8a")
    private String id;

    @Schema(description = "ID público do usuário (pode ser diferente do ID interno).", example = "user-12345")
    private String userId;
}
