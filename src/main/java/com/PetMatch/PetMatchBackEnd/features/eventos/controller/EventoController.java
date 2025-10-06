package com.PetMatch.PetMatchBackEnd.features.eventos.controller;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/eventos")
@AllArgsConstructor
public class EventoController {


    private EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodosEventos());
    }

    @PostMapping
    public ResponseEntity<EventoResponseDto> criarNovoEvento(@RequestBody CriarEventoDto eventoDto) {
        // IMPORTANTE: Em um sistema real, o ID e o perfil do usuário viriam
        // de um sistema de autenticação (ex: JWT Token).
        // Aqui, vamos simular para o exemplo.
        UUID idDaOngLogada = UUID.fromString("8a8a8a8a-8a8a-8a8a-8a8a-8a8a8a8a8a8a"); // Exemplo: ID da ONG logada
        String perfilDaOng = "ONG"; // Exemplo: Perfil da ONG logada

        EventoResponseDto novoEvento = eventoService.criarEvento(eventoDto, idDaOngLogada, perfilDaOng);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDto> buscarEventoPorId(@PathVariable UUID id) {
        return eventoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable UUID id) {
        eventoService.deletarPorId(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content
    }
}
