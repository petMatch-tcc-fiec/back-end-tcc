package com.PetMatch.PetMatchBackEnd.features.animais.controller;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalResponseDto;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/animais")
@AllArgsConstructor
@Tag(name = "Animais", description = "Endpoints para gerenciamento de animais da plataforma PetMatch.")
public class AnimaisController {

    private final AnimaisService animaisService;

    @Operation(
            summary = "Listar todos os animais",
            description = "Retorna uma lista de animais. Adotantes e Admins veem todos. ONGs veem apenas os seus próprios animais.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de animais retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<AnimalResponseDto>> listarAnimais(
            @AuthenticationPrincipal Usuario usuarioAutenticado // <-- 1. RECEBE O USUÁRIO LOGADO
    ) {
        // 2. PASSA O USUÁRIO PARA O SERVICE FAZER A LÓGICA DE FILTRO
        return ResponseEntity.ok(animaisService.findAllAnimaisDto(usuarioAutenticado));
    }

    @Operation(
            summary = "Cadastrar novo animal",
            description = "Cadastra um novo animal vinculado à ONG logada. Requer 'multipart/form-data'.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Animal cadastrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (não é ONG)")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // <-- IMPORTANTE para upload
    public ResponseEntity<AnimalResponseDto> criarNovoAnimal(
            @ModelAttribute AnimalRegisterDto dto, // <-- @ModelAttribute para form-data
            @RequestParam(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // Extrai ID e Perfil (exatamente como EventoController)
        UUID idDaOngLogada = usuarioAutenticado.getId();
        String perfilDaOng = usuarioAutenticado.getAccessLevel().name();

        AnimalResponseDto novoAnimal = animaisService.create(dto, file, idDaOngLogada, perfilDaOng);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAnimal);
    }

    @Operation(
            summary = "Buscar animal por ID",
            description = "Retorna os detalhes de um animal. Adotantes/Admins veem qualquer animal. ONGs só podem ver os seus.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (ONG tentando ver animal de outra ONG)"),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> buscarAnimalPorId(
            @Parameter(description = "ID do animal", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioAutenticado // <-- RECEBE O USUÁRIO LOGADO
    ) {
        // PASSA O USUÁRIO E O ID PARA O SERVICE
        // O Service tratará o 404 (se não existir) e o 403 (se não for dono)
        // (Exatamente como no EventoController)
        return animaisService.findAnimalDtoById(id, usuarioAutenticado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar animal por ID",
            description = "Atualiza os dados de um animal (apenas o dono).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (não é o dono)"),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> atualizarAnimal(
            @Parameter(description = "ID do animal a ser atualizado")
            @PathVariable UUID id,
            @RequestBody AnimalRegisterDto dto,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // Extrai ID e Perfil
        UUID idUsuarioLogado = usuarioAutenticado.getId();
        String perfilUsuario = usuarioAutenticado.getAccessLevel().name();

        AnimalResponseDto animalAtualizado = animaisService.update(id, dto, idUsuarioLogado, perfilUsuario);
        return ResponseEntity.ok(animalAtualizado);
    }


    @Operation(
            summary = "Deletar animal",
            description = "Remove um animal do sistema (apenas o dono).",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Animal deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (não é o dono)"),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAnimal(
            @Parameter(description = "ID do animal a ser deletado", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // Extrai ID e Perfil (exatamente como EventoController)
        UUID idUsuarioLogado = usuarioAutenticado.getId();
        String perfilUsuario = usuarioAutenticado.getAccessLevel().name();

        animaisService.deleteById(id, idUsuarioLogado, perfilUsuario);
        return ResponseEntity.noContent().build();
    }
}