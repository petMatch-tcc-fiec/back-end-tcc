package com.PetMatch.PetMatchBackEnd.features.user.services.impl;

import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.services.OngUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OngUsuariosServiceimpl implements OngUsuariosService {

    private final OngUsuariosRepository ongUsuariosRepository;

    public OngUsuariosServiceimpl(OngUsuariosRepository ongUsuariosRepository) {
        this.ongUsuariosRepository = ongUsuariosRepository;
    }

    @Override
    public OngUsuarios save(OngUsuarios ongUsuarios) {
        // Criptografa a senha antes de salvar
        if (ongUsuarios.getSenhaOng() != null) {
            ongUsuarios.setSenhaOng(PasswordEncryptor.encrypt(ongUsuarios.getSenhaOng()));
        }
        return ongUsuariosRepository.save(ongUsuarios);
    }

    @Override
    public Optional<OngUsuarios> findById(UUID id) {
        return ongUsuariosRepository.findById(id);
    }

    @Override
    public Optional<OngUsuarios> findByEmail(String email_ong) {
        return ongUsuariosRepository.findByEmail(email_ong);
    }

    @Override
    public List<OngUsuarios> findAll() {
        return ongUsuariosRepository.findAll();
    }

    @Override
    public OngUsuarios update(UUID id, OngUsuarios updatedOng) {
        return ongUsuariosRepository.findById(id).map(ongUsuarios -> {
            ongUsuarios.setEnderecoOng(updatedOng.getEnderecoOng());
            ongUsuarios.setTelefoneOng(updatedOng.getTelefoneOng());
            ongUsuarios.setCelularOng(updatedOng.getCelularOng());
            ongUsuarios.setCnpjOng(updatedOng.getCnpjOng());
            ongUsuarios.setEmailOng(updatedOng.getEmailOng());
            ongUsuarios.setSenhaOng(updatedOng.getSenhaOng());

            // Re-criptografa a senha apenas se uma nova for fornecida
            if (updatedOng.getSenhaOng() != null && !updatedOng.getSenhaOng().isEmpty()) {
                ongUsuarios.setSenhaOng(PasswordEncryptor.encrypt(updatedOng.getSenhaOng()));
            }
            return ongUsuariosRepository.save(ongUsuarios);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        ongUsuariosRepository.deleteById(id);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return ongUsuariosRepository.findByEmail(email).orElseThrow();
    }
}
