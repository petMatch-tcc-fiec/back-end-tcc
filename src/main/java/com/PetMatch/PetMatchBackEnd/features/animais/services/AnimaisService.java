package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimaisService {
    Animais create(Animais animais);
    Optional<Animais> findById(UUID id);
    List<Animais> findAll();
    List<Animais> findAllWithQueries(AnimalSearch animalSearch);
    Optional<Animais> update(UUID id, Animais updatedAnimal);
    void deleteById(UUID id);
}
