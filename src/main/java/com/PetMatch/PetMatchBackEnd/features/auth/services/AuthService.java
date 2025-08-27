package com.PetMatch.PetMatchBackEnd.features.auth.services;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;

public interface AuthService {
    AdotanteUsuarios register(RegisterRequest request);
    AdotanteUsuarios login(LoginRequest request);
}
