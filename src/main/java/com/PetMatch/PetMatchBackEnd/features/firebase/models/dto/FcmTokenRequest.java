package com.PetMatch.PetMatchBackEnd.features.firebase.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "FcmTokenRequest",
        description = "Objeto usado para registrar ou atualizar o token FCM do usuário autenticado."
)
public class FcmTokenRequest {

    @Schema(
            description = "Token FCM (Firebase Cloud Messaging) do dispositivo do usuário.",
            example = "e8f1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r9s0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "O token FCM não pode ser vazio.")
    private String fcmToken;
}
