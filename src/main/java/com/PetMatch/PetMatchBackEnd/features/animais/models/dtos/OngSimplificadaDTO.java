package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OngSimplificadaDTO {
    private String nomeFantasiaOng;
    // SEM senha, SEM usuario completo
}
