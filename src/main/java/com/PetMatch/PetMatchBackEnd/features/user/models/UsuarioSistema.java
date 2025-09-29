package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UsuarioSistema {

    @OneToOne
    Usuario usuario;
}
