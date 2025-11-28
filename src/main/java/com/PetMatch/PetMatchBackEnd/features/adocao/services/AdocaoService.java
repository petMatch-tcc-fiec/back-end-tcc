package com.PetMatch.PetMatchBackEnd.features.adocao.services;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario; // Import necessário

import java.util.List;
import java.util.UUID;

public interface AdocaoService {
    void registrarInteresse(UUID animalId, UUID usuarioId);
    List<InteresseResponseDTO> listarInteressadosPorAnimal(UUID animalId);
    void avaliarInteresse(UUID interesseId, AdocaoStatus novoStatus);

    // ✨ NOVO MÉTODO
    List<InteresseResponseDTO> listarMeusInteresses(Usuario usuarioLogado);
}