package com.PetMatch.PetMatchBackEnd.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adminusuarios")
@Schema(description = "Entidade que representa um usuário administrador do sistema.")
public class AdminUsuarios extends UsuarioSistema {

    @Schema(description = "Nome completo do administrador.", example = "Carlos Pereira")
    @Column(name = "nome_admin", nullable = false)
    private String nomeAdmin;

    @Schema(description = "Endereço de e-mail único do administrador.", example = "carlos.pereira@petmatch.com")
    @Column(unique = true, nullable = false, name = "email_admin")
    private String emailAdmin;

    @Schema(description = "Senha criptografada do administrador.", example = "$2a$10$XyZ123...")
    @Column(nullable = false, name = "senha_admin")
    private String senhaAdmin;

    @Schema(description = "CPF ou CNPJ único do administrador.", example = "123.456.789-00")
    @Column(unique = true, nullable = false, name = "cpf_cnpj_admin")
    private String cpfOuCnpjAdmin;
}
