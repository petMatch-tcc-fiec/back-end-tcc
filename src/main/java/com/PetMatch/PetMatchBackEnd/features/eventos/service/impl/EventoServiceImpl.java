package com.PetMatch.PetMatchBackEnd.features.eventos.service.impl;

import com.PetMatch.PetMatchBackEnd.features.eventos.dto.CriarEventoDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.dto.EventoResponseDto;
import com.PetMatch.PetMatchBackEnd.features.eventos.model.Evento;
import com.PetMatch.PetMatchBackEnd.features.eventos.repository.EventoRepository;
import com.PetMatch.PetMatchBackEnd.features.eventos.service.EventoService;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UsuarioRepository usuarioRepository;


    @Override
    public EventoResponseDto criarEvento(CriarEventoDto eventoDto, UUID idUsuarioLogado, String perfilUsuario) {
        // 1. Validação de Permissão (Regra de Negócio)
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new SecurityException("Acesso negado: Somente ONGs podem criar eventos.");
        }

        // 2. BUSCAR O OBJETO 'Usuario' PRIMEIRO
        // (Assumindo que o ID 'idUsuarioLogado' é o PK da entidade Usuario/UsuarioSistema)
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário de sistema não encontrado com o ID: " + idUsuarioLogado));

        // 3. AGORA, USE SEU MÉTODO para encontrar o perfil da ONG
        OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Não foi possível encontrar uma ONG associada ao usuário: " + usuario.getUsername())); // ou usuario.getEmail()


        // 4. Criação da Entidade
        Evento novoEvento = new Evento();
        novoEvento.setNome(eventoDto.getNome());
        novoEvento.setDataHora(eventoDto.getDataHora());
        novoEvento.setEndereco(eventoDto.getEndereco());

        // 5. Associa o evento à ONG logada usando o ID CORRETO (o PK da ONG)
        novoEvento.setIdOng(ong.getId()); // <-- AQUI ESTÁ A CORREÇÃO

        // 6. Persistência no Banco
        Evento eventoSalvo = eventoRepository.save(novoEvento);

        // 7. Mapeamento para o DTO de Resposta
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
                .id(evento.getIdOng())
                .build();
    }
}
