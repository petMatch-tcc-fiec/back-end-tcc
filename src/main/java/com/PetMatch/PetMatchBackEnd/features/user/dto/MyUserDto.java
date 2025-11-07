package com.PetMatch.PetMatchBackEnd.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Informações do usuário autenticado ou recuperado do sistema.")
public class MyUserDto {

    @Schema(description = "Nome completo do usuário.", example = "Maria Oliveira")
    private String nome;

    @Schema(description = "Endereço de e-mail do usuário.", example = "maria.oliveira@email.com")
    private String email;

    @Schema(description = "Número do CNPJ (caso seja uma ONG).", example = "12.345.678/0001-90")
    private String cnpj;

    @Schema(description = "Número do CPF (caso seja pessoa física).", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Tipo do usuário (ex: ADMIN, ONG, ADOTANTE).", example = "ONG")
    private String tipo;

    @Schema(description = "URL da foto de perfil do usuário.")
    private String picture;

    @Schema(description = "Endereço do usuário.", example = "Rua das Flores, 123 - Centro, Indaiatuba/SP")
    private String endereco;

    @Schema(description = "Número de celular do usuário.", example = "+55 19 91234-5678")
    private String celular;

    @Schema(description = "Número de telefone fixo do usuário.", example = "19 3344-5566")
    private String telefone;
}
