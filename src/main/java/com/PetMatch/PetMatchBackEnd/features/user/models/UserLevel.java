package com.PetMatch.PetMatchBackEnd.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;

/**
 * Enum que representa os níveis de acesso disponíveis no sistema PetMatch.
 * Implementa GrantedAuthority para integração direta com o Spring Security.
 */
@Schema(description = "Níveis de acesso dos usuários no sistema.")
public enum UserLevel implements GrantedAuthority {

    @Schema(description = "Usuário comum, interessado em adoções.")
    ADOTANTE,

    @Schema(description = "Administrador do sistema, com permissões elevadas.")
    ADMIN,

    @Schema(description = "Organização responsável por cadastrar e gerenciar animais.")
    ONG;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
