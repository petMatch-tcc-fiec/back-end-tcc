package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FotoAnimalDTO {
    private String arquivoAnimal;
    // SEM referência de volta ao Animal
}
