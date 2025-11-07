package com.PetMatch.PetMatchBackEnd.features.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(name = "CriarEventoDto", description = "Objeto usado para criar um novo evento.")
public class CriarEventoDto {

    @Schema(
            description = "Nome do evento.",
            example = "Feira de Adoção PetMatch"
    )
    private String nome;

    @Schema(
            description = "Data e hora em que o evento ocorrerá.",
            example = "2025-12-20T14:00:00"
    )
    private LocalDateTime dataHora;

    @Schema(
            description = "Endereço completo onde o evento será realizado.",
            example = "Praça Central, Indaiatuba - SP"
    )
    private String endereco;
}
