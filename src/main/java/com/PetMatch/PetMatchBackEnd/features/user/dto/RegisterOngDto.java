package com.PetMatch.PetMatchBackEnd.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterOngDto extends UsuarioDto{
    String endereco;
    String telefone;
    String celular;
    @NotNull
    String cnpj;
}
