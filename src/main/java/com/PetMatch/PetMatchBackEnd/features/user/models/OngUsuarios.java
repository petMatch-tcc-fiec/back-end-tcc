package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "OngUsuarios")
public class OngUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id_ong;

    @Column
    private String endereco_ong;

    @Column
    private String telefone_ong;

    @Column
    private String celular_ong;

    @Column
    private String cnpj_ong;

    @Column(unique = true, nullable = false)
    private String email_ong;

    @Column(nullable = false)
    private String senha_ong;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.ONG;
}
