package com.PetMatch.PetMatchBackEnd.features.firebase.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FcmTokenRequest {

    // O token é o dado principal e deve ser obrigatório.
    @NotBlank(message = "O token FCM não pode ser vazio.")
    private String fcmToken;
}
