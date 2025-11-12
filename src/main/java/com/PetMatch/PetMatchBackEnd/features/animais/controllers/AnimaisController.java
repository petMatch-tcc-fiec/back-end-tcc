package com.PetMatch.PetMatchBackEnd.features.animais.controllers;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalResponseDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/animais")
@Tag(name = "Animais", description = "Gerencia o cadastro, busca e atualização de informações sobre os animais disponíveis para adoção.")
public class AnimaisController {

    private final AnimaisService animaisService;

    public AnimaisController(AnimaisService animaisService) {
        this.animaisService = animaisService;
    }

    @Operation(
            summary = "Cadastrar um novo animal",
            description = "Cria um novo registro de animal disponível para adoção."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Animal criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animais.class))
    )
    @PostMapping
    public ResponseEntity<AnimalResponseDto> create(
            @Valid @RequestBody
            @Parameter(description = "Dados do novo animal a ser cadastrado")
            AnimalRegisterDto animais,
            Authentication authentication
    ) {
        AnimalResponseDto novoAnimal = animaisService.create(animais, authentication); // ✅ Retorna DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAnimal);
    }

    @Operation(
            summary = "Listar todos os animais",
            description = "Retorna uma lista com todos os animais cadastrados no sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de animais retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animais.class))
    )
    @GetMapping
    public ResponseEntity<List<Animais>> getAllAnimais() {
        List<Animais> animais = animaisService.findAll();
        return ResponseEntity.ok(animais);
    }

    @Operation(
            summary = "Buscar animal por ID",
            description = "Recupera as informações de um animal específico a partir do seu UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Animal encontrado"),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Animais> getAnimalById(
            @Parameter(description = "UUID do animal a ser buscado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        return animaisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar informações de um animal",
            description = "Atualiza os dados de um animal existente com base no seu UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Animais> updateAnimal(
            @Parameter(description = "UUID do animal a ser atualizado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @RequestBody
            @Parameter(description = "Novos dados do animal")
            Animais animais
    ) {
        return animaisService.update(id, animais)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deletar um animal",
            description = "Remove um animal do sistema com base em seu UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Animal deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(
            @Parameter(description = "UUID do animal a ser deletado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        if (animaisService.findById(id).isPresent()) {
            animaisService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Filtrar animais por critérios de busca",
            description = "Permite buscar animais aplicando filtros personalizados, como espécie, porte, idade, etc."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de animais filtrada retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animais.class))
    )
    @GetMapping("/filters/all")
    public List<Animais> getAnimalById(
            @Parameter(description = "Parâmetros de busca aplicados aos filtros de animais")
            AnimalSearch animalSearch
    ) {
        return animaisService.findAllWithQueries(animalSearch);
    }
}
