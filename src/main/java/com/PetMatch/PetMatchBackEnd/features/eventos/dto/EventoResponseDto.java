package com.PetMatch.PetMatchBackEnd.features.eventos.dto;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.OngSimplificadaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(name = "EventoResponseDto", description = "Objeto retornado pela API contendo os detalhes de um evento.")
public class EventoResponseDto {
    private UUID id;
    private String nome;
    private LocalDateTime dataHora;
    private String endereco;
    private OngSimplificadaDTO ong;
    // --- NOVO CAMPO ADICIONADO ---
    @Schema(description = "Descrição detalhada do evento.")
    private String descricao;
    // --- FIM DO NOVO CAMPO ---
}

