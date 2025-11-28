package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.FichaMedicaAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FichaMedicaRepository extends JpaRepository<FichaMedicaAnimal, UUID> {

    // Busca a ficha baseada no ID do animal (Relacionamento OneToOne)
    Optional<FichaMedicaAnimal> findByAnimalId(UUID animalId);
}
