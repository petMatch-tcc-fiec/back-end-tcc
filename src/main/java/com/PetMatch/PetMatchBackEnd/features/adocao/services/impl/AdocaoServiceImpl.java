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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdocaoServiceImpl implements AdocaoService {

    @Autowired
    private AdocaoInteresseRepository adocaoInteresseRepository;

    @Autowired
    private AnimaisRepository animaisRepository;

    @Autowired
    private UsuarioRepository userRepository;

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
        AdocaoInteresse interesse = adocaoInteresseRepository.findById(interesseId) // findById com UUID
                .orElseThrow(() -> new EntityNotFoundException("Interesse de adoção não encontrado com o ID: " + interesseId));

        if (novoStatus == AdocaoStatus.PENDENTE) {
            throw new IllegalArgumentException("Não é possível alterar o status para PENDENTE.");
        }

        interesse.setStatus(novoStatus);
        adocaoInteresseRepository.save(interesse);
    }
}
