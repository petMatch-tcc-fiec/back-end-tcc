package com.PetMatch.PetMatchBackEnd.features.adocao.services;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;

import java.util.List;
import java.util.UUID;

public interface AdocaoService {

    void registrarInteresse(UUID animalId, UUID usuarioId);

    List<InteresseResponseDTO> listarInteressadosPorAnimal(UUID animalId);

    void avaliarInteresse(UUID interesseId, AdocaoStatus novoStatus);
}
