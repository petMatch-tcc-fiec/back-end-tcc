package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AnimalSearch {
    private String nome;

    private Integer idade;

    private String porte;

    private String sexo;

    private String especie;

    private String raca;

    private String cor;

    private String sortBy;

    private SortOrder sortOrder;
}
