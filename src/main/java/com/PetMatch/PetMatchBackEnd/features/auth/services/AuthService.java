package com.PetMatch.PetMatchBackEnd.features.auth.services;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;

public interface AuthService {
    Usuario register(RegisterRequest request);
    Usuario login(LoginRequest request);
}
