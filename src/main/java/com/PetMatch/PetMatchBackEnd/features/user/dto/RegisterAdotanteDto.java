package com.PetMatch.PetMatchBackEnd.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdotanteDto extends UsuarioDto {
    @NotNull
    String cpf;
    String endereco;
    String celular;
    String descricaoOutrosAnimais;
    String preferencia;
}
