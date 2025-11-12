package com.PetMatch.PetMatchBackEnd.features.animais.repositories;

import com.PetMatch.PetMatchBackEnd.features.animais.models.FotosAnimais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FotosAnimaisRepository extends JpaRepository<FotosAnimais, UUID> {
}
