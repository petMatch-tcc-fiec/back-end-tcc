package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoAnimalDTO {
    // MUDANÇA: Adicionamos o ID e renomeamos o campo
    private UUID idFotoAnimal;
    private String url; // O frontend vai receber a URL neste campo
}
