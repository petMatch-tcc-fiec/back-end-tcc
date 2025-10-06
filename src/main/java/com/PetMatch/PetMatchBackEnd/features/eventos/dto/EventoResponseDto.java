package com.PetMatch.PetMatchBackEnd.features.eventos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventoResponseDto {
    private UUID id;
    private String nome;
    private LocalDateTime dataHora;
    private String endereco;
    private UUID ongId;
}
