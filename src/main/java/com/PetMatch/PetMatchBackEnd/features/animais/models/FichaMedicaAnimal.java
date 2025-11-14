package com.PetMatch.PetMatchBackEnd.features.animais.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "fichamedicaanimal")
@Schema(
        name = "FichaMedicaAnimal",
        description = "Contém informações médicas e histórico de saúde de um animal."
)
public class FichaMedicaAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da ficha médica.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idFichaMedica;

    @Column(name = "vacinas")
    @Schema(description = "Lista de vacinas aplicadas no animal.", example = "V8, Antirrábica")
    private String vacinas;

    @Column(name = "historico_saude")
    @Schema(description = "Resumo do histórico de saúde e tratamentos do animal.",
            example = "Tratado para pulgas e carrapatos recentemente. Nenhuma doença crônica.")
    private String historicoSaude;

    @OneToOne
    @JoinColumn(name = "fk_animais_id_animal", nullable = false)
    @Schema(description = "Animal associado a esta ficha médica.", implementation = Animais.class)
    private Animais animal;
}
