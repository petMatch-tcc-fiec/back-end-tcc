package com.PetMatch.PetMatchBackEnd.features.adocao.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AdocaoStatus",
        description = "Representa o status atual de um processo de adoção de animal."
)
public enum AdocaoStatus {

    @Schema(description = "O interesse foi registrado, mas ainda não foi avaliado.")
    PENDENTE,

    @Schema(description = "O interesse foi aprovado e a adoção está em andamento ou concluída.")
    APROVADO,

    @Schema(description = "O interesse foi rejeitado pelo administrador ou não atende aos critérios de adoção.")
    REJEITADO
}
