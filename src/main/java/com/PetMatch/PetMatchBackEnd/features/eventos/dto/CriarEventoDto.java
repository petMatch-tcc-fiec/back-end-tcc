package com.PetMatch.PetMatchBackEnd.features.eventos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CriarEventoDto {
    private String nome;
    private LocalDateTime dataHora;
    private String endereco;
}
