package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalCustomRepository {
    List<Animais> findAnimais(AnimalSearch animalSearch);
}
