package com.PetMatch.PetMatchBackEnd.features.auth.services.impl;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService;

    public AuthServiceImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setName(request.getName());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setAccessLevel(UserLevel.ADOTANTE);
        usuario.setPicture(request.getPicture());

        return usuarioService.save(usuario);
    }

    @Override
    public Usuario login(LoginRequest request) {
        return usuarioService.findByEmail(request.getEmail()).filter(usuario -> PasswordEncryptor.matches(request.getPassword(), usuario.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));
    }
}