package com.PetMatch.PetMatchBackEnd.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados possíveis do processo de registro de um usuário.")
public enum RegisterState {

    @Schema(description = "Usuário criado, mas ainda sem informações de perfil.")
    USER_CREATED,

    @Schema(description = "Perfil do usuário criado, mas sem imagem de perfil.")
    PROFILE_CREATED,

    @Schema(description = "Imagem de perfil do usuário adicionada e cadastro completo.")
    IMAGE_CREATED
}
