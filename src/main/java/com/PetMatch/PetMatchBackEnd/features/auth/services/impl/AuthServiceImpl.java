package com.PetMatch.PetMatchBackEnd.features.auth.services.impl;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestAdotante;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestOng;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.services.AdotanteUsuariosService;
import com.PetMatch.PetMatchBackEnd.features.user.services.OngUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    //ongUsuarios
    @Override
    public OngUsuarios registerOng(RegisterRequestOng request) {
        OngUsuarios ongUsuarios = new OngUsuarios();
        ongUsuarios.setEnderecoOng(request.getEnderecoOng());
        ongUsuarios.setTelefoneOng(request.getTelefoneOng());
        ongUsuarios.setCelularOng(request.getCelularOng());
        ongUsuarios.setCnpjOng(request.getCnpjOng());
        ongUsuarios.setEmailOng(request.getEmailOng());
        ongUsuarios.setSenhaOng(request.getSenhaOng());

        return ongUsuariosService.save(ongUsuarios);
    }

    @Override
    public UserDetails login(LoginRequest request) {
        // Tenta encontrar o usuário como adotante
        return adotanteUsuariosService.findByEmail(request.getEmail())
                .map(adotante -> {
                    if (PasswordEncryptor.matches(request.getSenha(), adotante.getSenhaAdotante())) {
                        return (UserDetails) adotante;
                    }
                    throw new BadCredentialsException("Email ou senha inválidos.");
                })
                .or(() -> {
                    // Se não for um adotante, tenta encontrar como ONG
                    return ongUsuariosService.findByEmail(request.getEmail())
                            .map(ong -> {
                                if (PasswordEncryptor.matches(request.getSenha(), ong.getSenhaOng())) {
                                    return (UserDetails) ong;
                                }
                                throw new BadCredentialsException("Email ou senha inválidos.");
                            });
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}