package com.PetMatch.PetMatchBackEnd.features.eventos.service;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario; // Importar Usuario
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoService {

    EventoResponseDto criarEvento(CriarEventoDto eventoDto, UUID idDaOngLogada, String perfilDaOng);

    // MODIFICADO: Deve receber o usuário para filtrar
    List<EventoResponseDto> listarTodosEventos(Usuario usuarioAutenticado);

    // MODIFICADO: Deve receber o usuário para checar permissão
    Optional<EventoResponseDto> buscarPorId(UUID id, Usuario usuarioAutenticado);

    void deletarPorId(UUID idEvento, UUID idUsuarioLogado, String perfilUsuario);
}