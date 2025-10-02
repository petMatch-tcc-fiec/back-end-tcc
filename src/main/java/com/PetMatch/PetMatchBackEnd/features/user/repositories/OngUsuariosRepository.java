package com.PetMatch.PetMatchBackEnd.features.user.repositories;

import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OngUsuariosRepository extends JpaRepository<OngUsuarios, String> {
    Optional<OngUsuarios> findByUsuario(Usuario usuario);
}
