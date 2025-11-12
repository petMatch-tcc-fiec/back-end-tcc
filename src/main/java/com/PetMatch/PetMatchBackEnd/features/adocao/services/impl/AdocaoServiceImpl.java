package com.PetMatch.PetMatchBackEnd.features.adocao.services.impl;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import com.PetMatch.PetMatchBackEnd.features.adocao.repositories.AdocaoInteresseRepository;
import com.PetMatch.PetMatchBackEnd.features.adocao.services.AdocaoService;
import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.repositories.AnimaisRepository;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    private UsuarioRepository userRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void registrarInteresse(UUID animalId, UUID usuarioId) { // Parâmetros UUID
        boolean jaInteressado = adocaoInteresseRepository.existsByIdAndUsuarioIdAndStatus(animalId, usuarioId, AdocaoStatus.PENDENTE);
        if (jaInteressado) {
            throw new IllegalStateException("Usuário já está na fila de espera para este animal.");
        }

        Animais animal = animaisRepository.findById(animalId) // findById com UUID
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com o ID: " + animalId));

        Usuario usuario = userRepository.findById(usuarioId) // findById com UUID
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        AdocaoInteresse novoInteresse = new AdocaoInteresse();
        novoInteresse.setAnimal(animal);
        novoInteresse.setUsuario(usuario);

        adocaoInteresseRepository.save(novoInteresse);
    }

    @Override
    public List<InteresseResponseDTO> listarInteressadosPorAnimal(UUID animalId) { // Parâmetro UUID
        if (!animaisRepository.existsById(animalId)) { // existsById com UUID
            throw new EntityNotFoundException("Animal não encontrado com o ID: " + animalId);
        }

        List<AdocaoInteresse> interesses = adocaoInteresseRepository.findByAnimalIdAndStatusOrderByDataDeCriacaoAsc(animalId, AdocaoStatus.PENDENTE);

        return interesses.stream()
                .map(InteresseResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void avaliarInteresse(UUID interesseId, AdocaoStatus novoStatus) { // Parâmetro UUID
        AdocaoInteresse interesse = adocaoInteresseRepository.findById(interesseId)
                .orElseThrow(() -> new EntityNotFoundException("Interesse de adoção não encontrado com o ID: " + interesseId));

        if (novoStatus == AdocaoStatus.PENDENTE) {
            throw new IllegalArgumentException("Não é possível alterar o status para PENDENTE.");
        }

        // 1. Lógica para APROVAÇÃO
        // Verifica se houve uma transição para APROVADO (e se não estava APROVADO antes)
        boolean foiAprovado = novoStatus == AdocaoStatus.APROVADO && interesse.getStatus() != AdocaoStatus.APROVADO;

        // 2. Lógica para REJEIÇÃO
        // Verifica se houve uma transição para REPROVADO (e se não estava REPROVADO antes)
        boolean foiRejeitado = novoStatus == AdocaoStatus.REJEITADO && interesse.getStatus() != AdocaoStatus.REJEITADO;

        // Atualiza o status e salva no banco de dados
        interesse.setStatus(novoStatus);
        adocaoInteresseRepository.save(interesse);

        // Envio de E-mails:

        if (foiAprovado) {
            // Método que você já deve ter:
            enviarEmailAprovacao(interesse);
        }

        // Novo bloco para enviar e-mail de REJEIÇÃO
        if (foiRejeitado) {
            enviarEmailReprovacao(interesse); // 👈 NOVO MÉTODO
        }
    }

    public void enviarEmailAprovacao(AdocaoInteresse interesse) {
        try {
            Usuario usuario = interesse.getUsuario();
            Animais animal = interesse.getAnimal(); // Supondo que você queira o nome do animal

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@petmatch.com"); // Ou seu e-mail configurado
            message.setTo(usuario.getEmail()); // Pega o email do usuário associado ao interesse
            message.setSubject("Parabéns! Sua solicitação de adoção foi aprovada!");
            message.setText("Olá " + usuario.getName() + ",\n\n" +
                    "Temos ótimas notícias! Seu interesse em adotar o(a) " + animal.getNome() + " foi aprovado.\n\n" +
                    "A ONG responsável entrará em contato em breve para os próximos passos.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe PetMatch");
            emailSender.send(message);
            log.info("E-mail de aprovação enviado para: {}", usuario.getEmail());

        } catch (Exception e) {
            // Logar o erro é importante para saber se o e-mail falhou
            log.error("Erro ao enviar e-mail de aprovação para {}: {}", interesse.getUsuario().getEmail(), e.getMessage());
            // Você pode querer adicionar um tratamento mais robusto aqui (ex: colocar em uma fila para tentar reenviar)
        }
    }
    public void enviarEmailReprovacao(AdocaoInteresse interesse) {
        try {
            Usuario usuario = interesse.getUsuario();
            Animais animal = interesse.getAnimal(); // Supondo que você queira o nome do animal

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@petmatch.com"); // Ou seu e-mail configurado
            message.setTo(usuario.getEmail()); // Pega o email do usuário associado ao interesse
            message.setSubject("Que pena! Sua solicitação de adoção foi reprovada!");
            message.setText("Olá " + usuario.getName() + ",\n\n" +
                    "Temos más notícias! Seu interesse em adotar o(a) " + animal.getNome() + " foi reprovado.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe PetMatch");
            emailSender.send(message);
            log.info("E-mail de reprovação enviado para: {}", usuario.getEmail());

        } catch (Exception e) {
            // Logar o erro é importante para saber se o e-mail falhou
            log.error("Erro ao enviar e-mail de reprovação para {}: {}", interesse.getUsuario().getEmail(), e.getMessage());
            // Você pode querer adicionar um tratamento mais robusto aqui (ex: colocar em uma fila para tentar reenviar)
        }
    }
}
