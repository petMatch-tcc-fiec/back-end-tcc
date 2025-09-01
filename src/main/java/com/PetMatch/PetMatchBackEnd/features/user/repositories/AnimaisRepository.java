package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimaisRepository extends JpaRepository<Animais, UUID> {

    Optional<Animais> findBySexo(String sexo);
    Optional<Animais> findByRaca(String raca);
}
