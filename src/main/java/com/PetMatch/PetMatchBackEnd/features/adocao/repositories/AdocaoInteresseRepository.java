package com.PetMatch.PetMatchBackEnd.features.adocao.repositories;

import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.models.AdocaoInteresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdocaoInteresseRepository extends JpaRepository<AdocaoInteresse, UUID> {

    // Para a ONG (já existe)
    @Query("SELECT i FROM AdocaoInteresse i WHERE i.animal.id = :animalId AND i.status = :status ORDER BY i.dataDeCriacao ASC")
    List<AdocaoInteresse> buscarInteressesPorAnimal(@Param("animalId") UUID animalId, @Param("status") AdocaoStatus status);

    boolean existsByAnimalIdAndUsuarioIdAndStatus(UUID animalId, UUID usuarioId, AdocaoStatus status);

    boolean existsByAnimalIdAndUsuarioId(UUID animalId, UUID usuarioId);

    // ✨ NOVO: Para o Adotante (buscar pelo ID da tabela AdotanteUsuarios)
    List<AdocaoInteresse> findByUsuarioId(UUID adotanteId);
}