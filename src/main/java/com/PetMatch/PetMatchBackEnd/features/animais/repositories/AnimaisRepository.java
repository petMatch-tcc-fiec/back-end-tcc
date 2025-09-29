package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimaisRepository extends JpaRepository<Animais, UUID> {

    Optional<Animais> findBySexo(String sexo);
    Optional<Animais> findByRaca(String raca);
}
