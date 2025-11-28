package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder // Adicionado para permitir a construção no ServiceImpl
@NoArgsConstructor
@AllArgsConstructor
public class FichaMedicaDTO {
    private UUID idFicha;      // Necessário para o front saber se já existe
    private UUID idAnimal;     // Necessário para vincular
    private String vacinas;
    private String historicoSaude;
}