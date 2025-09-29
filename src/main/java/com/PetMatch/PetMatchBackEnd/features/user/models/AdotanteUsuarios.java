package com.PetMatch.PetMatchBackEnd.features.user.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "AdotanteUsuarios")
public class AdotanteUsuarios extends UsuarioSistema{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_adotante")
    private UUID idAdotante;

    @Column(name = "nome_adotante")
    private String nomeAdotante;

    @Column(unique = true, nullable = false, name = "cpf_adotante")
    private String cpfAdotante;

    @Column(name = "endereco_adotante")
    private String enderecoAdotante;

    @Column(nullable = false, name = "celular_adotante")
    private String celularAdotante;

    @Column(unique = true, nullable = false, name = "email_adotante")
    private String emailAdotante;

    @Column(nullable = false, name = "senha_adotante")
    private String senhaAdotante;

    @Column(name = "descricao_outros_animais")
    private String descricaoOutrosAnimais;

    @Column
    private String preferencia;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.ADOTANTE;

}