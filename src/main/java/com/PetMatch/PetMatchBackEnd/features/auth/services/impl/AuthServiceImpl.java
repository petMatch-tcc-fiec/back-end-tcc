package com.PetMatch.PetMatchBackEnd.features.auth.services.impl;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public Usuario register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setName(request.getName());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setAccessLevel(UserLevel.ADOTANTE);
        usuario.setPicture(request.getPicture());

        return usuarioService.save(usuario);
    }

    @Override
    public Usuario login(LoginRequest request) {
        Optional<Usuario> usuario = usuarioService.findByEmail(request.getEmail());
        if (usuario.isPresent()) {
            Usuario encontrado = usuario.get();
            if (PasswordEncryptor.matches(request.getPassword(), encontrado.getPassword()))
                return encontrado;
        }
        throw new BadCredentialsException("Email ou senha inválidos.");
    }
}