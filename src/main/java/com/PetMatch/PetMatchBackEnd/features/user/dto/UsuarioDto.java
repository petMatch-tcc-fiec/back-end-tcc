package com.PetMatch.PetMatchBackEnd.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO base para cadastro de usuários no sistema.")
public abstract class UsuarioDto {

    @Schema(description = "Nome completo do usuário.",
            example = "Maria da Silva",
            required = true)
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String name;

    @Schema(description = "Endereço de e-mail válido do usuário.",
            example = "maria.silva@email.com",
            required = true)
    @Email(message = "Por favor, insira um endereço de e-mail válido.")
    private String email;

    @Schema(description = "Senha com no mínimo 8 caracteres, contendo ao menos uma letra maiúscula e um caractere especial.",
            example = "SenhaSegura@123",
            required = true)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, incluindo pelo menos uma letra maiúscula e um caractere especial."
    )
    private String password;
}
