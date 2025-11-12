package com.PetMatch.PetMatchBackEnd.features.user.models;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "fotosadotante")
@Schema(
        name = "FotosAdotante",
        description = "Representa uma foto associada a um adotante."
)
public class FotosAdotante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_foto")
    @Schema(description = "Identificador único da foto do adotante.")
    private UUID idFoto;

    @Column(name = "arquivo_animal", nullable = false)
    @Schema(description = "Caminho ou URL do arquivo da foto do adotante.")
    private String arquivo;

    @ManyToOne
    @JoinColumn(name = "fk_adotanteusuarios_id_adotante", nullable = false)
    @Schema(description = "Adotante ao qual esta foto pertence.")
    private AdotanteUsuarios adotanteUsuarios;
}
