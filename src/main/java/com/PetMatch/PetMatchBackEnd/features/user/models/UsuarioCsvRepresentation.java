package com.PetMatch.PetMatchBackEnd.features.user.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioCsvRepresentation {

    // ------------------------------------
    // Campos da entidade Usuario
    // ------------------------------------

    @CsvBindByName(column = "email", required = true)
    private String email;

    @CsvBindByName(column = "password", required = true)
    private String password;

    @CsvBindByName(column = "name")
    private String name;

    // O nível de acesso (ADMIN, STANDARD, GUEST) define quais campos extras serão preenchidos.
    @CsvBindByName(column = "accessLevel", required = true)
    private String accessLevel;

    // ------------------------------------
    // Campos de Admin
    // ------------------------------------

    @CsvBindByName(column = "cpf_cnpj_admin")
    private String cnpjOuCnpjAdmin;

    // ------------------------------------
    // Campos de Adotante
    // ------------------------------------

    @CsvBindByName(column = "cpf_adotante")
    private String cpfAdotante;

    @CsvBindByName(column = "endereco_adotante")
    private String enderecoAdotante;

    @CsvBindByName(column = "celular_adotante")
    private String celularAdotante;

    @CsvBindByName(column = "descricao_outros_animais")
    private String descricaoOutrosAnimais;

    @CsvBindByName(column = "preferencia")
    private String preferencia;

    // ------------------------------------
    // Campos de Ong
    // ------------------------------------

    @CsvBindByName(column = "enedereco_ong")
    private String enderecoOng;

    @CsvBindByName(column = "telefone_ong")
    private String telefoneOng;

    @CsvBindByName(column = "celular_ong")
    private String celularOng;

    @CsvBindByName(column = "cnpj_ong")
    private String cnpjOng;

}
