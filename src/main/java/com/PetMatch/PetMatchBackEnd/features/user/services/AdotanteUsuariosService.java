package com.PetMatch.PetMatchBackEnd.features.user.services;


import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdotanteUsuariosService {
    AdotanteUsuarios save(AdotanteUsuarios adotanteUsuarios);
    Optional<AdotanteUsuarios> findById(UUID id);
    Optional<AdotanteUsuarios> findByEmail(String email);
    List<AdotanteUsuarios> findAll();
    AdotanteUsuarios update(UUID id, AdotanteUsuarios updatedUser);
    void deleteById(UUID id);
}