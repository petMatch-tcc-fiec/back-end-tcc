package com.PetMatch.PetMatchBackEnd.features.animais.services;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimaisService {
    Animais create(Animais animais);
    Optional<Animais> findById(UUID id);
    List<Animais> findBySexo(String sexo);
    List<Animais> findByRaca(String raca);
    List<Animais> findByCor(String cor);
    List<Animais> findByPorte(String porte);
    List<Animais> findByEspecie(String tipo);
    List<Animais> findAll();
    Optional<Animais> update(UUID id, Animais updatedAnimal);
    void deleteById(UUID id);
}
