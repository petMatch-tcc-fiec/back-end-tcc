package com.PetMatch.PetMatchBackEnd.features.firebase.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
        name = "NotificationMessage",
        description = "Objeto usado para enviar uma notificação push via Firebase para um usuário específico."
)
public class NotificationMessage {

    @Schema(
            description = "ID do usuário autenticado que receberá a notificação.",
            example = "9d8f7a6b-5c4d-3e2f-1a0b-9c8d7e6f5a4b",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String authUserId;

    @Schema(
            description = "Título da notificação.",
            example = "Novo evento de adoção disponível!"
    )
    private String title;

    @Schema(
            description = "Mensagem de corpo da notificação.",
            example = "Confira agora os pets disponíveis para adoção neste fim de semana 🐶🐱"
    )
    private String message;
}
