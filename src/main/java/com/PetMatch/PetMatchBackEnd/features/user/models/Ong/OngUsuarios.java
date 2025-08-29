package com.PetMatch.PetMatchBackEnd.features.user.models.Ong;

import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "OngUsuarios")
public class OngUsuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_ong")
    private UUID idOng;

    @Column(name = "endereco_ong")
    private String enderecoOng;

    @Column(name = "telefone_ong")
    private String telefoneOng;

    @Column(name = "celular_ong")
    private String celularOng;

    @Column(name = "cnpj_ong")
    private String cnpjOng;

    @Column(unique = true, nullable = false, name = "email_ong")
    private String emailOng;

    @Column(nullable = false, name = "senha_ong")
    private String senhaOng;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.ONG;
}
