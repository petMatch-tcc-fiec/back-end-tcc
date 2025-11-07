package com.PetMatch.PetMatchBackEnd.features.animais.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "fotosAnimais")
@Schema(
        name = "FotosAnimais",
        description = "Representa uma foto associada a um animal disponível para adoção."
)
public class FotosAnimais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador único da foto do animal.", example = "4b5bdb52-69aa-44b0-b5b0-d7b20f12eabc")
    private UUID idFotoAnimal;

    @Column(name = "arquivo_animal", nullable = false)
    @Schema(
            description = "Caminho ou URL do arquivo da foto do animal.",
            example = "https://petmatch.s3.amazonaws.com/fotos/luna-1.jpg"
    )
    private String arquivoAnimal;

    @ManyToOne
    @JoinColumn(name = "fk_animais_id_animal", nullable = false)
    @Schema(description = "Animal ao qual esta foto pertence.", implementation = Animais.class)
    private Animais animal;
}
