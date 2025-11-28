package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.FichaMedicaDTO;

import java.util.UUID;

public interface FichaMedicaService {

    FichaMedicaDTO salvarOuAtualizar(UUID idAnimal, FichaMedicaDTO dto, UUID idUsuarioLogado);

    // E esta também:
    FichaMedicaDTO buscarPorAnimal(UUID idAnimal);
}
