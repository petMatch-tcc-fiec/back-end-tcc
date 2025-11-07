package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "RegisterRequest",
        description = "Objeto de requisição para o registro de novos usuários no sistema PetMatch."
)
public class RegisterRequest {

    @Schema(description = "Nome completo do usuário.", example = "Maria Oliveira")
    private String name;

    @Schema(description = "Endereço de e-mail único do usuário.", example = "maria.oliveira@example.com")
    private String email;

    @Schema(description = "Senha escolhida pelo usuário para acesso à conta.", example = "SenhaForte123!")
    private String password;

    @Schema(description = "URL da imagem de perfil do usuário (opcional).", example = "https://petmatch.s3.amazonaws.com/perfis/maria.jpg")
    private String picture;

    @Schema(description = "Nível de acesso do usuário dentro da plataforma.", implementation = UserLevel.class, example = "USER")
    private UserLevel accessLevel;
}
