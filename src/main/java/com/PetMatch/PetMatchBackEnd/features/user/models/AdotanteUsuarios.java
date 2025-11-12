package com.PetMatch.PetMatchBackEnd.features.user.models;

import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "adotanteusuarios")
@Schema(description = "Entidade que representa um usuário adotante no sistema.")
public class AdotanteUsuarios extends UsuarioSistema {

    @Schema(description = "Nome completo do adotante.", example = "Fernanda Oliveira")
    @Column(name = "nome_adotante", nullable = false)
    private String nomeAdotante;

    @Schema(description = "CPF único do adotante.", example = "123.456.789-10")
    @Column(unique = true, nullable = false, name = "cpf_adotante")
    private String cpfAdotante;

    @Schema(description = "Endereço residencial do adotante.", example = "Rua das Palmeiras, 123 - Indaiatuba/SP")
    @Column(name = "endereco_adotante")
    private String enderecoAdotante;

    @Schema(description = "Número de celular para contato.", example = "(19) 99999-1234")
    @Column(nullable = false, name = "celular_adotante")
    private String celularAdotante;

    @Schema(description = "E-mail único do adotante.", example = "fernanda.oliveira@email.com")
    @Column(unique = true, nullable = false, name = "email_adotante")
    private String emailAdotante;

    @Schema(description = "Senha criptografada do adotante.", example = "$2a$10$XyZ123...")
    @Column(nullable = false, name = "senha_adotante")
    private String senhaAdotante;

    @Schema(description = "Descrição sobre outros animais que o adotante possui.", example = "Tenho um gato adulto e um cachorro de pequeno porte.")
    @Column(name = "descricao_outros_animais")
    private String descricaoOutrosAnimais;

    @Schema(description = "Preferência de adoção (ex: filhote, adulto, porte pequeno).", example = "Filhote de porte pequeno")
    @Column(name = "preferencia")
    private String preferencia;

    @OneToMany(mappedBy = "adotanteUsuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotosAdotante> fotosAdotantes = new ArrayList<>();
}
