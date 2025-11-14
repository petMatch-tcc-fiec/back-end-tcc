package com.PetMatch.PetMatchBackEnd.features.eventos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(name = "EventoResponseDto", description = "Objeto retornado pela API contendo os detalhes de um evento.")
public class EventoResponseDto {

    @Schema(
            description = "Identificador único do evento."
    )
    private UUID id;

    @Schema(
            description = "Nome do evento.",
            example = "Feira de Adoção PetMatch"
    )
    private String nome;

    @Schema(
            description = "Data e hora do evento.",
            example = "2025-12-20T14:00:00"
    )
    private LocalDateTime dataHora;

    @Schema(
            description = "Endereço completo do evento.",
            example = "Praça Central, Indaiatuba - SP"
    )
    private String endereco;

    @Schema(
            description = "Identificador da ONG responsável pelo evento."
    )
    private UUID idOng;
}

