package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdminUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminUsuariosRepository extends JpaRepository<AdminUsuarios, UUID> {
    Optional<AdminUsuarios> findByUser(Usuario usuario);
}
