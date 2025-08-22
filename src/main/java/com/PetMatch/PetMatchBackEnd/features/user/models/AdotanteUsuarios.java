package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "AdotanteUsuarios")
public class AdotanteUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id_adotante;

    @Column
    private String endereco_adotante;

    @Column(unique = true, nullable = false)
    private String cpf_adotante;

    @Column
    private String telefone_adotante;

    @Column(nullable = false)
    private String celular_adotante;

    @Column(unique = true, nullable = false)
    private String email_adotante;

    @Column(nullable = false)
    private String senha_adotante;

    @Column
    private String nome_adotante;

    @Column
    private String descricao_outros_animais;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.USER;

}