package com.PetMatch.PetMatchBackEnd.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdminDto extends UsuarioDto{

    @NotNull
    String cpfOuCnpj;
}
