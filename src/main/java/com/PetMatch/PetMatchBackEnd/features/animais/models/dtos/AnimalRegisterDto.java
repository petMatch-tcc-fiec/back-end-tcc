package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnimalRegisterDto {
    private String nome;
    private Integer idade;
    private String porte;
    private String sexo;
    private String especie;
    private String raca;
    private String cor;
    private String observacoesAnimal;
    private UUID ongId;
}
