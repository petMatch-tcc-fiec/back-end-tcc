package com.PetMatch.PetMatchBackEnd.features.adocao.dtos;

import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class InteresseResponseDTO {

    private UUID interesseId;
    private UUID usuarioId;
    private String nomeUsuario;
    private LocalDateTime dataDeInteresse;

    public InteresseResponseDTO(AdocaoInteresse interesse) {
        this.interesseId = interesse.getId();
        this.usuarioId = interesse.getUsuario().getId();
        this.nomeUsuario = interesse.getUsuario().getName();
        this.dataDeInteresse = interesse.getDataDeCriacao();
    }
}
