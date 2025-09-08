package com.PetMatch.PetMatchBackEnd.features.auth.services.impl;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestAdotante;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.services.AdotanteUsuariosService;
import com.PetMatch.PetMatchBackEnd.features.user.services.OngUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdotanteUsuariosService adotanteUsuariosService;

    private final OngUsuariosService ongUsuariosService;

    public AuthServiceImpl(AdotanteUsuariosService adotanteUsuariosService, OngUsuariosService ongUsuariosService) {
        this.adotanteUsuariosService = adotanteUsuariosService;
        this.ongUsuariosService = ongUsuariosService;
    }

    //adotanteUsuarios
    @Override
    public AdotanteUsuarios register(RegisterRequestAdotante request) {
        AdotanteUsuarios adotanteUsuarios = new AdotanteUsuarios();
        adotanteUsuarios.setNomeAdotante(request.getNome());
        adotanteUsuarios.setCpfAdotante(request.getCpf());
        adotanteUsuarios.setEnderecoAdotante(request.getEndereco());
        adotanteUsuarios.setCelularAdotante(request.getCelular());
        adotanteUsuarios.setEmail(request.getEmail());
        adotanteUsuarios.setSenhaAdotante(request.getSenha());
        adotanteUsuarios.setDescricaoOutrosAnimais(request.getDescricaoOutrosAnimais());
        adotanteUsuarios.setPreferencia(request.getPreferencia());

        return adotanteUsuariosService.save(adotanteUsuarios);
    }

    @Override
    public AdotanteUsuarios login(LoginRequest request) {
        return adotanteUsuariosService.findByEmail(request.getEmail())
                .filter(adotanteUsuarios -> PasswordEncryptor.matches(request.getSenha(), adotanteUsuarios.getSenhaAdotante()))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));
    }

    //ongUsuarios
    @Override
    public OngUsuarios register(RegisterRequestAdotante request) {
        OngUsuarios ongUsuarios = new OngUsuarios();
        ongUsuarios.setEnderecoOng(request.getEndereco());
        ongUsuarios.setTelefoneOng(request.getCpf());
        ongUsuarios.setCelularOng(request.getCelular());
        ongUsuarios.setCnpjOng(request.getCelular());
        ongUsuarios.setEmailOng(request.getEmail());
        ongUsuarios.setSenhaOng(request.getSenha());

        return ongUsuariosService.save(ongUsuarios);
    }

    @Override
    public AdotanteUsuarios login(LoginRequest request) {
        return adotanteUsuariosService.findByEmail(request.getEmail())
                .filter(adotanteUsuarios -> PasswordEncryptor.matches(request.getSenha(), adotanteUsuarios.getSenhaAdotante()))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));
    }
}