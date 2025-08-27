package com.PetMatch.PetMatchBackEnd.features.auth.services.impl;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.services.AdotanteUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdotanteUsuariosService adotanteUsuariosService;

    public AuthServiceImpl(AdotanteUsuariosService adotanteUsuariosService) {
        this.adotanteUsuariosService = adotanteUsuariosService;
    }

    @Override
    public AdotanteUsuarios register(RegisterRequest request) {
        AdotanteUsuarios adotanteUsuarios = new AdotanteUsuarios();
        adotanteUsuarios.setNome_adotante(request.getNome());
        adotanteUsuarios.setCpf_adotante(request.getCpf());
        adotanteUsuarios.setEndereco_adotante(request.getEndereco());
        adotanteUsuarios.setCelular_adotante(request.getCelular());
        adotanteUsuarios.setEmail_adotante(request.getEmail());
        adotanteUsuarios.setSenha_adotante(request.getSenha());
        adotanteUsuarios.setDescricao_outros_animais(request.getDescricao_outros_animais());
        adotanteUsuarios.setPreferencia(request.getPreferencia());

        return adotanteUsuariosService.save(adotanteUsuarios);
    }

    @Override
    public AdotanteUsuarios login(LoginRequest request) {
        return adotanteUsuariosService.findByEmail(request.getEmail())
                .filter(adotanteUsuarios -> PasswordEncryptor.matches(request.getSenha(), adotanteUsuarios.getSenha_adotante()))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));
    }
}