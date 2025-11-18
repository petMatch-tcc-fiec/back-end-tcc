package com.PetMatch.PetMatchBackEnd.features.adocao.dtos;

import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Schema(
        name = "InteresseResponseDTO",
        description = "Representa o interesse de um usuário em adotar um animal, com informações básicas do interessado."
)
public class InteresseResponseDTO {

    @Schema(
            description = "Identificador único do interesse de adoção",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID interesseId;

    @Schema(
            description = "Identificador único do usuário interessado",
            example = "c84e180d-8c2d-4a3e-a98e-123456789abc"
    )
    private UUID usuarioId;

    @Schema(
            description = "Nome do usuário que demonstrou interesse na adoção",
            example = "Maria Silva"
    )
    private String nomeUsuario;

    @Schema(
            description = "Data e hora em que o interesse foi registrado",
            example = "2025-11-07T18:45:00"
    )
    private LocalDateTime dataDeInteresse;

    public InteresseResponseDTO(AdocaoInteresse interesse) {
        this.interesseId = interesse.getId();
        this.usuarioId = interesse.getUsuario().getId();
        this.nomeUsuario = interesse.getUsuario().getNomeAdotante();
        this.dataDeInteresse = interesse.getDataDeCriacao();
    }
}
