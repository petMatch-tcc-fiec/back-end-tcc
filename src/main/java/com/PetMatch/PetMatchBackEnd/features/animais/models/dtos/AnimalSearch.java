package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "AnimalSearch",
        description = "Objeto usado para aplicar filtros e ordenação na busca de animais."
)
public class AnimalSearch {

    @Schema(description = "Nome do animal para busca parcial.", example = "Luna")
    private String nome;

    @Schema(description = "Idade aproximada do animal em anos.", example = "3")
    private Integer idade;

    @Schema(description = "Porte físico do animal (pequeno, médio, grande).", example = "Médio")
    private String porte;

    @Schema(description = "Sexo do animal.", example = "Fêmea")
    private String sexo;

    @Schema(description = "Espécie do animal (por exemplo: cachorro, gato).", example = "Cachorro")
    private String especie;

    @Schema(description = "Raça do animal, caso aplicável.", example = "Vira-lata")
    private String raca;

    @Schema(description = "Cor predominante do animal.", example = "Branco e marrom")
    private String cor;

    @Schema(description = "Campo usado para ordenação dos resultados.", example = "nome")
    private String sortBy;

    @Schema(description = "Ordem da ordenação (ASC para crescente, DESC para decrescente).", example = "ASC")
    private SortOrder sortOrder;
}
