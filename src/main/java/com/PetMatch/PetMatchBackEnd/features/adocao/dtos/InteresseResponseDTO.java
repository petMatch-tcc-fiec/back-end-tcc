package com.PetMatch.PetMatchBackEnd.features.adocao.dtos;

import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Schema(name = "InteresseResponseDTO", description = "Dados do interesse (inclui dados do Animal e do Usuário).")
public class InteresseResponseDTO {

    private UUID interesseId;
    private UUID usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
    private LocalDateTime dataDeInteresse;
    private String status;

    // Objeto Animal aninhado
    private AnimalResumoDTO animal;

    public InteresseResponseDTO(AdocaoInteresse interesse) {
        this.interesseId = interesse.getId();
        this.dataDeInteresse = interesse.getDataDeCriacao(); // Ajustei para getDataCriacao se for o nome correto na entidade
        this.status = interesse.getStatus() != null ? interesse.getStatus().toString() : "PENDENTE";

        // Mapeia Usuário
        if (interesse.getUsuario() != null && interesse.getUsuario().getUsuario() != null) {
            Usuario usuarioEntity = interesse.getUsuario().getUsuario();
            this.usuarioId = usuarioEntity.getId();
            this.nomeUsuario = usuarioEntity.getName();
            this.emailUsuario = usuarioEntity.getEmail();
        } else {
            this.nomeUsuario = "Usuário Desconhecido";
        }

        // Mapeia Animal (AGORA COM FOTOS E ID)
        if (interesse.getAnimal() != null) {

            // Lógica para recuperar a URL da primeira foto
            String fotoUrl = null;
            List<FotosAnimais> fotos = interesse.getAnimal().getFotosAnimais();

            if (fotos != null && !fotos.isEmpty()) {
                fotoUrl = fotos.get(0).getArquivoAnimal();
            }

            this.animal = new AnimalResumoDTO(
                    interesse.getAnimal().getId(), // ✨ ID Adicionado aqui
                    interesse.getAnimal().getNome(),
                    interesse.getAnimal().getRaca(),
                    interesse.getAnimal().getIdade() != null ? interesse.getAnimal().getIdade().toString() : "0",
                    fotoUrl // ✨ URL real aqui
            );
        }
    }

    // DTO Interno para o Animal
    @Data
    @AllArgsConstructor
    public static class AnimalResumoDTO {
        private UUID id; // ✨ Campo ID Adicionado (Vital para o Front-end)
        private String nome;
        private String raca;
        private String idade;
        private String imagemUrl;
    }
}