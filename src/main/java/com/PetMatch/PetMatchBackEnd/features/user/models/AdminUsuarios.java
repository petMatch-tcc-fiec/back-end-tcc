package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class AdminUsuarios extends UsuarioSistema{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_admin")
    private UUID idAdmin;

    @Column(name = "nome_admin")
    private String nomeAdmin;

    @Column(unique = true, nullable = false, name = "email_admin")
    private String emailAdmin;

    @Column(nullable = false, name = "senha_admin")
    private String senhaAdmin;

    @Column(unique = true, nullable = false, name = "cpf_cnpj_admin")
    private String cpfOuCnpjAdmin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.ADMIN;
}
