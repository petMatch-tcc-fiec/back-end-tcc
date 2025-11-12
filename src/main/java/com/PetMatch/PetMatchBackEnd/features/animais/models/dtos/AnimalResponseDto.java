package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponseDto {
    private UUID id;
    private String nome;
    private Integer idade;
    private String porte;
    private String sexo;
    private String especie;
    private String raca;
    private String cor;
    private String observacoesAnimal;
    private OngSimplificadaDTO ong;
    private FichaMedicaDTO fichaMedicaAnimal;
    private List<FotoAnimalDTO> fotosAnimais;
}
