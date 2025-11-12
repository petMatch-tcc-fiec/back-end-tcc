package com.PetMatch.PetMatchBackEnd.features.animais.models.dtos;

import com.PetMatch.PetMatchBackEnd.features.animais.models.FichaMedicaAnimal;
import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class AnimalRegisterDto {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do animal.", example = "Luna")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade não pode ser negativa")
    @Schema(description = "Idade aproximada do animal, em anos.", example = "3")
    private Integer idade;

    @NotBlank(message = "Porte é obrigatório")
    @Pattern(regexp = "^(Pequeno|Médio|Grande)$", message = "Porte deve ser: Pequeno, Médio ou Grande")
    @Schema(description = "Porte físico do animal (pequeno, médio, grande).", example = "Médio")
    private String porte;

    @NotBlank(message = "Sexo é obrigatório")
    @Pattern(regexp = "^[MF]$", message = "Sexo deve ser M ou F")
    @Schema(description = "Sexo do animal. (M = macho, F = fêmea)", example = "F")
    private String sexo;

    @NotBlank(message = "Espécie é obrigatória")
    @Pattern(regexp = "^(Cachorro|Gato)$", message = "Espécie deve ser: Cachorro ou Gato")
    @Schema(description = "Espécie do animal (exemplo: cachorro, gato).", example = "Cachorro")
    private String especie;

    @Schema(description = "Raça do animal, se conhecida.", example = "Vira-lata")
    private String raca;

    @Schema(description = "Cor predominante do animal.", example = "Branco e marrom")
    private String cor;

    @Schema(description = "Observações adicionais sobre o animal, como comportamento ou necessidades especiais.",
            example = "Muito dócil e se dá bem com outros cães.")
    private String observacoesAnimal;

    // Dados simplificados da ONG
    @Schema(description = "ONG responsável por este animal.")
    private OngSimplificadaDTO ong;

    // Dados médicos
    @Schema(description = "Ficha médica do animal.")
    private FichaMedicaDTO fichaMedicaAnimal;

    // Fotos
    @Schema(description = "Lista de fotos associadas ao animal.")
    private List<FotoAnimalDTO> fotosAnimais;
}
