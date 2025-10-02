package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdminUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUsuariosRepository extends JpaRepository<AdminUsuarios, String> {
    Optional<AdminUsuarios> findByUsuario(Usuario usuario);
}
