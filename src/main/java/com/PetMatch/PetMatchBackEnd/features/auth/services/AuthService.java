package com.PetMatch.PetMatchBackEnd.features.auth.services;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestAdotante;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestOng;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    AdotanteUsuarios register(RegisterRequestAdotante request);
    UserDetails login(LoginRequest request);
    OngUsuarios registerOng(RegisterRequestOng request);
}
