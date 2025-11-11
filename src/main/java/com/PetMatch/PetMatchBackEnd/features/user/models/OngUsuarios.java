package com.PetMatch.PetMatchBackEnd.features.user.models;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
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
@Table(name = "ongusuarios")
@Schema(description = "Entidade que representa uma ONG cadastrada no sistema.")
public class OngUsuarios extends UsuarioSistema {

    @Schema(description = "Nome fantasia da ONG.", example = "Amor Animal Indaiatuba")
    @Column(name = "nome_fantasia_ong", nullable = false)
    private String nomeFantasiaOng;

    @Schema(description = "Endereço de e-mail da ONG (único).", example = "contato@amoranimal.org")
    @Column(unique = true, nullable = false, name = "email_ong")
    private String emailOng;

    @Schema(description = "Senha criptografada da ONG.", example = "$2a$10$XyZ123...")
    @Column(nullable = false, name = "senha_ong")
    private String senhaOng;

    @Schema(description = "Endereço físico da ONG.", example = "Rua das Acácias, 200 - Indaiatuba/SP")
    @Column(name = "endereco_ong")
    private String enderecoOng;

    @Schema(description = "Telefone fixo da ONG.", example = "(19) 3834-4455")
    @Column(name = "telefone_ong")
    private String telefoneOng;

    @Schema(description = "Número de celular da ONG.", example = "(19) 98888-4455")
    @Column(name = "celular_ong")
    private String celularOng;

    @Schema(description = "CNPJ único da ONG.", example = "12.345.678/0001-99")
    @Column(unique = true, nullable = false, name = "cnpj_ong")
    private String cnpjOng;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de animais associados a esta ONG.", implementation = Animais.class)
    private List<Animais> animais = new ArrayList<>();
}
