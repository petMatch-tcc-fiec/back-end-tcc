package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;
}
