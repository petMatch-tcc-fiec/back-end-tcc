package com.PetMatch.PetMatchBackEnd.features.eventos.service.impl;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import com.PetMatch.PetMatchBackEnd.features.eventos.repository.EventoRepository;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public EventoResponseDto criarEvento(CriarEventoDto eventoDto, UUID idOng, String perfilUsuario) {
        // 1. Validação de Permissão (Regra de Negócio)
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new SecurityException("Acesso negado: Somente ONGs podem criar eventos.");
        }

        // 2. Validação de Existência (Regra de Negócio)
        // Verifica se a ONG que está tentando criar o evento realmente existe no banco.
        ongUsuariosRepository.findById(idOng)
                .orElseThrow(() -> new RuntimeException("ONG não encontrada com o ID: " + idOng));

        // 3. Criação da Entidade
        Evento novoEvento = new Evento();
        novoEvento.setNome(eventoDto.getNome());
        novoEvento.setDataHora(eventoDto.getDataHora());
        novoEvento.setEndereco(eventoDto.getEndereco());
        novoEvento.setIdOng(idOng); // Associa o evento à ONG logada

        // 4. Persistência no Banco
        Evento eventoSalvo = eventoRepository.save(novoEvento);

        // 5. Mapeamento para o DTO de Resposta
        return mapToEventoResponseDto(eventoSalvo);
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
                .idOng(evento.getIdOng())
                .build();
    }
}
