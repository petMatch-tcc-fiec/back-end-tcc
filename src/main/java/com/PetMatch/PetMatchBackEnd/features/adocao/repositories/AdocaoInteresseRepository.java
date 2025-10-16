package com.PetMatch.PetMatchBackEnd.features.adocao.repositories;

import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdocaoInteresseRepository extends JpaRepository<AdocaoInteresse, UUID> {

    List<AdocaoInteresse> findByAnimalIdAndStatusOrderByDataDeCriacaoAsc(UUID animalId, AdocaoStatus status);


    boolean existsByIdAndUsuarioIdAndStatus(UUID animalId, UUID usuarioId, AdocaoStatus status);
}
