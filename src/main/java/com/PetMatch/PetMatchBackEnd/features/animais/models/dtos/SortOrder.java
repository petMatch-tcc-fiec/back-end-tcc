package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SortOrder",
        description = "Define a direção da ordenação dos resultados."
)
public enum SortOrder {

    @Schema(description = "Ordenação crescente (A → Z ou 0 → 9).")
    ASC,

    @Schema(description = "Ordenação decrescente (Z → A ou 9 → 0).")
    DESC
}
