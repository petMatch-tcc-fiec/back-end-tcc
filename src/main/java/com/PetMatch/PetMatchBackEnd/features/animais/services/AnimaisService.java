package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalRegisterDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalResponseDto;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimaisService {
    AnimalResponseDto create(AnimalRegisterDto dto, MultipartFile imagem, Authentication authentication);
    Optional<Animais> findById(UUID id);
    List<Animais> findAll();
    List<Animais> findAllWithQueries(AnimalSearch animalSearch);
    Optional<Animais> update(UUID id, Animais updatedAnimal);
    void deleteById(UUID id);
    List<AnimalResponseDto> findAllAnimaisDto();
}
