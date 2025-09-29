package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimaisService {
    OngUsuarios save(Animais animais);
    Optional<Animais> findById(UUID id);
    Optional<Animais> findBySexo(String sexo);
    Optional<Animais> findByRaca(String raca);
    List<Animais> findAll();
    Optional<Animais> update(UUID id, Animais updatedAnimal);
    void deleteById(UUID id);
}
