package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder // Adicionado para permitir a construção no ServiceImpl
@NoArgsConstructor
@AllArgsConstructor
public class FichaMedicaDTO {
    // Note: Se sua Entidade FichaMedicaAnimal tem 'castrado', 'vacinado', 'doencasExistentes',
    // este DTO não vai refletir 100% a Entidade. Se necessário, me envie a Entidade FichaMedicaAnimal.
    private String vacinas;
    private String historicoSaude;
}