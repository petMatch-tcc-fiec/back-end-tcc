package com.PetMatch.PetMatchBackEnd.features.eventos.service;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoService {

    EventoResponseDto criarEvento(CriarEventoDto eventoDto, UUID idDaOngLogada, String perfilDaOng);
    List<EventoResponseDto> listarTodosEventos();
    Optional<EventoResponseDto> buscarPorId(UUID id);
    void deletarPorId(UUID idEvento, UUID idUsuarioLogado, String perfilUsuario);
}
