package com.PetMatch.PetMatchBackEnd.features.eventos.service.impl;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import com.PetMatch.PetMatchBackEnd.features.eventos.repository.EventoRepository;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventoServiceImpl implements EventoService {


    private EventoRepository eventoRepository;
    private final OngUsuariosRepository ongUsuariosRepository;

    @Override
    @Transactional
    public Evento criarEvento(CriarEventoDto eventoDto, Authentication authentication) {
        // ✅ Busca a ONG do usuário logado
        Usuario usuario = (Usuario) authentication.getPrincipal();

        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new AccessDeniedException("Usuário não é uma ONG"));

        // ✅ Cria evento vinculado à ONG do usuário
        Evento evento = Evento.builder()
                .nome(eventoDto.getNome())
                .dataHora(eventoDto.getDataHora())
                .endereco(eventoDto.getEndereco())
                .ong(ong) // ✅ ONG do usuário logado, não do request
                .build();

        return eventoRepository.save(evento);
    }

    @Override
    public List<EventoResponseDto> listarTodosEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::mapToEventoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventoResponseDto> buscarPorId(UUID id) {
        return eventoRepository.findById(id).map(this::mapToEventoResponseDto);
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado com o ID: " + id);
        }
        eventoRepository.deleteById(id);
    }

    private EventoResponseDto mapToEventoResponseDto(Evento evento) {
        return EventoResponseDto.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .dataHora(evento.getDataHora())
                .endereco(evento.getEndereco())
                .build();
    }
}
