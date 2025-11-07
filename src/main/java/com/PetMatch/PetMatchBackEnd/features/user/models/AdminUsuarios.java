package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AdminUsuarios")
public class AdminUsuarios extends UsuarioSistema{

    @Column(name = "nome_admin")
    private String nomeAdmin;

    @Column(unique = true, nullable = false, name = "email_admin")
    private String emailAdmin;

    @Column(nullable = false, name = "senha_admin")
    private String senhaAdmin;

    @Column(unique = true, nullable = false, name = "cpf_cnpj_admin")
    private String cpfOuCnpjAdmin;

}
