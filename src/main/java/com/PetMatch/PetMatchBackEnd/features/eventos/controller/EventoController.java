package com.PetMatch.PetMatchBackEnd.features.eventos.controller;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/eventos")
@AllArgsConstructor
@Tag(name = "Eventos", description = "Endpoints para gerenciamento de eventos da plataforma PetMatch.")
public class EventoController {

    private final EventoService eventoService;

    @Operation(
            summary = "Listar todos os eventos",
            description = "Retorna uma lista com todos os eventos cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodosEventos());
    }

    @Operation(
            summary = "Criar novo evento",
            description = "Cria um novo evento vinculado à ONG logada (simulado neste exemplo).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do evento")
            }
    )
    @PostMapping
    public ResponseEntity<Evento> criarNovoEvento(
            @Valid @RequestBody CriarEventoDto eventoDto, Authentication authentication) {
        Evento novoEvento = eventoService.criarEvento(eventoDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @Operation(
            summary = "Buscar evento por ID",
            description = "Retorna os detalhes de um evento específico, com base no seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Evento encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDto> buscarEventoPorId(
            @Parameter(description = "ID do evento", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id) {
        return eventoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deletar evento",
            description = "Remove um evento do sistema com base em seu ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(
            @Parameter(description = "ID do evento a ser deletado", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id) {
        eventoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}

