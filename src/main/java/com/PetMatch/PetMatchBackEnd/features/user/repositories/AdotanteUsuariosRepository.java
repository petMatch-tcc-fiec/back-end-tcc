package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdotanteUsuariosRepository extends JpaRepository<AdotanteUsuarios, String> {
    Optional<AdotanteUsuarios> findByUsuario(Usuario usuario);
    Optional<AdotanteUsuarios> findByUsuarioId(UUID usuarioId);
}
