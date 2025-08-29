package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdotanteUsuariosRepository extends JpaRepository<AdotanteUsuarios, UUID> {
    Optional<AdotanteUsuarios> findByEmail(String email);
}
