package com.PetMatch.PetMatchBackEnd.features.user.services;

import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OngUsuariosService {
    OngUsuarios save(OngUsuarios ongUsuarios);
    Optional<OngUsuarios> findById(UUID id);
    Optional<OngUsuarios> findByEmail(String email);
    List<OngUsuarios> findAll();
    OngUsuarios update(UUID id, OngUsuarios updatedOng);
    void deleteById(UUID id);
}
