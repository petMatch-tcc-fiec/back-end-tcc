package com.PetMatch.PetMatchBackEnd.features.adocao.services.impl;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import com.PetMatch.PetMatchBackEnd.features.adocao.repositories.AdocaoInteresseRepository;
import com.PetMatch.PetMatchBackEnd.features.adocao.services.AdocaoService;
import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.firebase.services.NotificationService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdotanteUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✨ Importante!

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdocaoServiceImpl implements AdocaoService {

    private static final Logger log = LoggerFactory.getLogger(AdocaoServiceImpl.class);

    @Autowired
    private AdocaoInteresseRepository adocaoInteresseRepository;

    @Autowired
    private AnimaisRepository animaisRepository;

    @Autowired
    private AdotanteUsuariosRepository adotanteUsuariosRepository;

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // ... (mantenha os imports e anotações existentes)

    @Override
    @Transactional(readOnly = true)
    public List<InteresseResponseDTO> listarMeusInteresses(Usuario usuarioLogado) {
        // 1. Descobrir o ID de Adotante deste Usuário
        AdotanteUsuarios adotante = adotanteUsuariosRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de adotante não encontrado. Complete seu cadastro."));

        // 2. Buscar lista na tabela de interesses usando o ID do Adotante
        List<AdocaoInteresse> meusInteresses = adocaoInteresseRepository.findByUsuarioId(adotante.getId());

        // 3. Converter para DTO (agora o DTO inclui os dados do animal)
        return meusInteresses.stream()
                .map(InteresseResponseDTO::new)
                .collect(Collectors.toList());
    }

// ... (restante do código)


    @Override
    @Transactional // ✨ Garante que a gravação seja atômica
    public void registrarInteresse(UUID animalId, UUID usuarioId) {

        Animais animal = animaisRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com o ID: " + animalId));

        AdotanteUsuarios adotante = adotanteUsuariosRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil de adotante não encontrado para o usuário: " + usuarioId));

        boolean jaExisteRegistro = adocaoInteresseRepository.existsByAnimalIdAndUsuarioId(animalId, adotante.getId());

        if (jaExisteRegistro) {
            // Retorna uma mensagem amigável para o frontend
            throw new IllegalStateException("Você já enviou uma solicitação para este animal anteriormente.");
        }

        AdocaoInteresse novoInteresse = new AdocaoInteresse();
        novoInteresse.setAnimal(animal);
        novoInteresse.setUsuario(adotante);
        // O status e data são definidos no @PrePersist da entidade, ou você pode setar aqui explicitamente:
        novoInteresse.setStatus(AdocaoStatus.PENDENTE);

        adocaoInteresseRepository.save(novoInteresse);
    }

    @Override
    @Transactional(readOnly = true) // ✨ CRUCIAL: Mantém a conexão aberta para carregar dados Lazy do usuário
    public List<InteresseResponseDTO> listarInteressadosPorAnimal(UUID animalId) {
        if (!animaisRepository.existsById(animalId)) {
            throw new EntityNotFoundException("Animal não encontrado com o ID: " + animalId);
        }

        // ✨ Usa a nova Query explícita
        List<AdocaoInteresse> interesses = adocaoInteresseRepository.buscarInteressesPorAnimal(animalId, AdocaoStatus.PENDENTE);

        return interesses.stream()
                .map(InteresseResponseDTO::new) // Agora a conversão funcionará pois a transação está aberta
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // ✨ Garante atualização segura
    public void avaliarInteresse(UUID interesseId, AdocaoStatus novoStatus) {
        AdocaoInteresse interesse = adocaoInteresseRepository.findById(interesseId)
                .orElseThrow(() -> new EntityNotFoundException("Interesse de adoção não encontrado com o ID: " + interesseId));

        if (novoStatus == AdocaoStatus.PENDENTE) {
            throw new IllegalArgumentException("Não é possível alterar o status para PENDENTE.");
        }

        boolean foiAprovado = novoStatus == AdocaoStatus.APROVADO && interesse.getStatus() != AdocaoStatus.APROVADO;
        boolean foiRejeitado = novoStatus == AdocaoStatus.REJEITADO && interesse.getStatus() != AdocaoStatus.REJEITADO;

        interesse.setStatus(novoStatus);
        adocaoInteresseRepository.save(interesse);

        if (foiAprovado) {
            enviarNotificacaoAprovacao(interesse);
        }

        if (foiRejeitado) {
            enviarNotificacaoReprovacao(interesse);
        }
    }

    public void enviarNotificacaoAprovacao(AdocaoInteresse interesse) {
        try {
            AdotanteUsuarios adotante = interesse.getUsuario();
            Usuario usuario = adotante.getUsuario();
            Animais animal = interesse.getAnimal();

            if (usuario.getFcmToken() != null && !usuario.getFcmToken().isEmpty()) {
                String title = "Parabéns! Sua adoção foi aprovada!";
                String body = "Temos ótimas notícias, " + usuario.getName() + "! Seu interesse em adotar o(a) " + animal.getNome() + " foi aprovado";
                notificationService.sendPushNotification(usuario.getFcmToken(), title, body);
            }
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de aprovação", e);
        }
    }

    public void enviarNotificacaoReprovacao(AdocaoInteresse interesse) {
        try {
            AdotanteUsuarios adotante = interesse.getUsuario();
            Usuario usuario = adotante.getUsuario();
            Animais animal = interesse.getAnimal();

            if (usuario.getFcmToken() != null && !usuario.getFcmToken().isEmpty()) {
                String title = "Atualização sobre sua adoção";
                String body = "Olá " + usuario.getName() + ", houve uma atualização no seu processo de adoção do(a) " + animal.getNome() + ".";
                notificationService.sendPushNotification(usuario.getFcmToken(), title, body);
            }
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de reprovação", e);
        }
    }
}