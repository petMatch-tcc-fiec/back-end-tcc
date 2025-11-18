package com.PetMatch.PetMatchBackEnd.features.eventos.service.impl;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.OngSimplificadaDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        novoEvento.setDescricao(eventoDto.getDescricao()); // <-- MUDANÇA AQUI

        // 5. Associa o evento à ONG logada usando o ID CORRETO (o PK da ONG)
        novoEvento.setIdOng(ong.getId()); // <-- AQUI ESTÁ A CORREÇÃO

        // 6. Persistência no Banco
        Evento eventoSalvo = eventoRepository.save(novoEvento);

        // 7. Mapeamento para o DTO de Resposta
        return mapToEventoResponseDto(eventoSalvo);
    }


    // --- LÓGICA DE PERMISSÃO APLICADA ---
    @Override
    public List<EventoResponseDto> listarTodosEventos(Usuario usuarioAutenticado) {
        String perfil = usuarioAutenticado.getAccessLevel().name();
        List<Evento> eventos;

        // Se for Adotante ou Admin, vê tudo
        if ("ADOTANTE".equalsIgnoreCase(perfil) || "ADMIN".equalsIgnoreCase(perfil)) {
            eventos = eventoRepository.findAll();
        }
        // Se for ONG, vê apenas os seus
        else if ("ONG".equalsIgnoreCase(perfil)) {
            OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuarioAutenticado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

            // Usa o novo método do repositório
            eventos = eventoRepository.findByIdOng(ong.getId());
        }
        // Outros perfis (se houver) não veem nada
        else {
            eventos = List.of();
        }

        return eventos.stream()
                .map(this::mapToEventoResponseDto)
                .collect(Collectors.toList());
    }

    // --- LÓGICA DE PERMISSÃO APLICADA ---
    @Override
    public Optional<EventoResponseDto> buscarPorId(UUID id, Usuario usuarioAutenticado) {
        // 1. Busca o evento
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado.")); // Lança 404 se não existir

        String perfil = usuarioAutenticado.getAccessLevel().name();

        // 2. Adotantes e Admins podem ver
        if ("ADOTANTE".equalsIgnoreCase(perfil) || "ADMIN".equalsIgnoreCase(perfil)) {
            return Optional.of(mapToEventoResponseDto(evento));
        }

        // 3. ONGs precisam ser a "dona"
        if ("ONG".equalsIgnoreCase(perfil)) {
            OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuarioAutenticado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado."));

            // 4. Verifica se o evento pertence à ONG logada
            if (evento.getIdOng().equals(ong.getId())) {
                return Optional.of(mapToEventoResponseDto(evento)); // Pertence, pode ver
            } else {
                // Não é dela, lança 403 - Proibido
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para visualizar este evento.");
            }
        }

        // Perfil desconhecido
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil de usuário inválido para esta ação.");
    }

    @Override
    public void deletarPorId(UUID idEvento, UUID idUsuarioLogado, String perfilUsuario) {
        // 1. Verificar se é uma ONG (opcional, mas bom)
        if (!"ONG".equalsIgnoreCase(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Somente ONGs podem deletar eventos.");
        }

        // 2. Buscar a ONG logada (mesma lógica do seu método criarEvento)
        Usuario usuario = usuarioRepository.findById(idUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário de sistema não encontrado."));

        OngUsuarios ongLogada = ongUsuariosRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil de ONG não encontrado para este usuário."));

        // 3. Buscar o Evento que ele quer deletar
        // (Se não achar, lança o 404 que falamos)
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado com o ID: " + idEvento));

        // 4. A VERIFICAÇÃO DE SEGURANÇA!
        //    Verificar se o ID da ONG logada é o mesmo ID da ONG salvo no evento.
        if (!evento.getIdOng().equals(ongLogada.getId())) {
            // Se não for o dono, lança 403 (Proibido)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Você não tem permissão para deletar este evento.");
        }

        // 5. Se passou em tudo, pode deletar
        eventoRepository.delete(evento);
    }

    // --- CORREÇÃO DO ERRO 404 E ADIÇÃO DO NOVO CAMPO ---
    private EventoResponseDto mapToEventoResponseDto(Evento evento) {
        // 1. Busca os dados da ONG associada
        OngUsuarios ong = ongUsuariosRepository.findById(evento.getIdOng())
                .orElseThrow(() -> new RuntimeException("BUG: ONG associada ao evento não encontrada. ID: " + evento.getIdOng()));

        // 2. Cria o DTO simplificado (CORRIGIDO)
        OngSimplificadaDTO ongDto = OngSimplificadaDTO.builder()
                .nomeFantasiaOng(ong.getNomeFantasiaOng()) // <--- CORREÇÃO AQUI
                .build();
        // O DTO OngSimplificadaDTO não pede email, então não o colocamos.

        // 3. Mapeamento final
        return EventoResponseDto.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .dataHora(evento.getDataHora())
                .endereco(evento.getEndereco())
                .descricao(evento.getDescricao()) // <-- MUDANÇA AQUI
                .ong(ongDto) // Mapeando o objeto OngSimplificadaDTO
                .build();
    }
}