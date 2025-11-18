package com.PetMatch.PetMatchBackEnd.features.eventos.controller;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;

import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            description = "Retorna uma lista de eventos. Adotantes e Admins veem todos. ONGs veem apenas os seus próprios eventos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listarEventos(
            @AuthenticationPrincipal Usuario usuarioAutenticado // <-- 1. RECEBE O USUÁRIO LOGADO
    ) {
        // 2. PASSA O USUÁRIO PARA O SERVICE FAZER A LÓGICA DE FILTRO
        return ResponseEntity.ok(eventoService.listarTodosEventos(usuarioAutenticado));
    }

    @Operation(
            summary = "Criar novo evento",
            description = "Cria um novo evento vinculado à ONG logada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do evento"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (não é ONG)")
            }
    )
    @PostMapping
    public ResponseEntity<EventoResponseDto> criarNovoEvento(
            @RequestBody CriarEventoDto eventoDto,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // A lógica de extração de ID e Perfil permanece
        UUID idDaOngLogada = usuarioAutenticado.getId();
        String perfilDaOng = usuarioAutenticado.getAccessLevel().name();

        EventoResponseDto novoEvento = eventoService.criarEvento(eventoDto, idDaOngLogada, perfilDaOng);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @Operation(
            summary = "Buscar evento por ID",
            description = "Retorna os detalhes de um evento. Adotantes/Admins veem qualquer evento. ONGs só podem ver os seus.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Evento encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (ONG tentando ver evento de outra ONG)"),
                    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDto> buscarEventoPorId(
            @Parameter(description = "ID do evento", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioAutenticado // <-- 3. RECEBE O USUÁRIO LOGADO
    ) {
        // 4. PASSA O USUÁRIO E O ID PARA O SERVICE
        // O Service tratará o 404 (se não existir) e o 403 (se não for dono)
        return eventoService.buscarPorId(id, usuarioAutenticado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // O 'orElseThrow' no service vai disparar antes se for 403 ou 404 de negócio
    }

    @Operation(
            summary = "Deletar evento",
            description = "Remove um evento do sistema (apenas o dono).",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (não é o dono)"),
                    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(
            @Parameter(description = "ID do evento a ser deletado", example = "a3f1e6d5-7c2b-4a89-8ef1-123456789abc")
            @PathVariable UUID id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // A lógica de extração de ID e Perfil permanece
        UUID idUsuarioLogado = usuarioAutenticado.getId();
        String perfilUsuario = usuarioAutenticado.getAccessLevel().name();

        eventoService.deletarPorId(id, idUsuarioLogado, perfilUsuario);
        return ResponseEntity.noContent().build();
    }
}