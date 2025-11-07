package com.PetMatch.PetMatchBackEnd.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto usado para registrar um usuário do tipo Adotante no sistema.")
public class RegisterAdotanteDto extends UsuarioDto {

    @NotNull(message = "O campo 'cpf' é obrigatório.")
    @Schema(description = "CPF do adotante.", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Endereço completo do adotante.", example = "Rua das Flores, 123 - Indaiatuba/SP")
    private String endereco;

    @Schema(description = "Número de celular do adotante.", example = "(19) 99999-8888")
    private String celular;

    @Schema(description = "Descrição de outros animais que o adotante possa ter.",
            example = "Tenho um cachorro e um gato, ambos vacinados.")
    private String descricaoOutrosAnimais;

    @Schema(description = "Preferências do adotante em relação ao tipo de animal desejado.",
            example = "Prefiro gatos adultos e calmos.")
    private String preferencia;
}
