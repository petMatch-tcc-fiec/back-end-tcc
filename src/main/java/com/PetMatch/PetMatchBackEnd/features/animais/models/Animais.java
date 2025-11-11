package com.PetMatch.PetMatchBackEnd.features.animais.models;

import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "animais")
@Schema(
        name = "Animais",
        description = "Representa um animal disponível para adoção no sistema PetMatch."
)
public class Animais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id_animal")
    private UUID id;

    @Column(name = "nome_animal")
    @Schema(description = "Nome do animal.", example = "Luna")
    private String nome;

    @Column(name = "idade_animal")
    @Schema(description = "Idade aproximada do animal, em anos.", example = "3")
    private Integer idade;

    @Column(name = "porte_animal")
    @Schema(description = "Porte físico do animal (pequeno, médio, grande).", example = "Médio")
    private String porte;

    @Column(name = "sexo_animal", length = 1)
    @Schema(description = "Sexo do animal. (M = macho, F = fêmea)", example = "F")
    private String sexo;

    @Column(name = "especie_animal")
    @Schema(description = "Espécie do animal (exemplo: cachorro, gato).", example = "Cachorro")
    private String especie;

    @Column(name = "raca_animal")
    @Schema(description = "Raça do animal, se conhecida.", example = "Vira-lata")
    private String raca;

    @Column(name = "cor_animal")
    @Schema(description = "Cor predominante do animal.", example = "Branco e marrom")
    private String cor;

    @Column(name = "observacoes_animal")
    @Schema(description = "Observações adicionais sobre o animal, como comportamento ou necessidades especiais.",
            example = "Muito dócil e se dá bem com outros cães.")
    private String observacoesAnimal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_ongUsuarios_id", nullable = false)
    @Schema(description = "ONG responsável por este animal.")
    private OngUsuarios ong;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL)
    @Schema(description = "Ficha médica do animal.", implementation = FichaMedicaAnimal.class)
    private FichaMedicaAnimal fichaMedicaAnimal;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de fotos associadas ao animal.", implementation = FotosAnimais.class)
    private List<FotosAnimais> fotosAnimais = new ArrayList<>();
}
