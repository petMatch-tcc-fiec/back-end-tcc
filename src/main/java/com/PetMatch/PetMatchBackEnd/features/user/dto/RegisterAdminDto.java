package com.PetMatch.PetMatchBackEnd.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto usado para registrar um administrador no sistema.")
public class RegisterAdminDto extends UsuarioDto {

    @NotNull(message = "O campo 'cpfOuCnpj' é obrigatório.")
    @Schema(
            description = "CPF ou CNPJ do administrador. Deve ser informado conforme o tipo de cadastro.",
            example = "123.456.789-00"
    )
    private String cpfOuCnpj;
}
