package com.PetMatch.PetMatchBackEnd.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto usado para registrar uma ONG no sistema.")
public class RegisterOngDto extends UsuarioDto {

    @Schema(description = "Endereço completo da ONG.", example = "Rua dos Animais, 456 - Indaiatuba/SP")
    private String endereco;

    @Schema(description = "Telefone fixo da ONG.", example = "(19) 3838-0000")
    private String telefone;

    @Schema(description = "Celular de contato da ONG.", example = "(19) 99999-0000")
    private String celular;

    @NotNull(message = "O campo 'cnpj' é obrigatório.")
    @Schema(description = "CNPJ da ONG.", example = "12.345.678/0001-99", required = true)
    private String cnpj;
}
