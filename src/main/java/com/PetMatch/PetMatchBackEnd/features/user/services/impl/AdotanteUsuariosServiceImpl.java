package com.PetMatch.PetMatchBackEnd.features.user.services.impl;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdotanteUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.services.AdotanteUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdotanteUsuariosServiceImpl implements AdotanteUsuariosService {

    private final AdotanteUsuariosRepository adotanteUsuariosRepository;

    public AdotanteUsuariosServiceImpl(AdotanteUsuariosRepository adotanteUsuariosRepository) {
        this.adotanteUsuariosRepository = adotanteUsuariosRepository;
    }

    @Override
    public AdotanteUsuarios save(AdotanteUsuarios adotanteUsuarios) {
        if (adotanteUsuarios.getSenha_adotante() != null) {
            adotanteUsuarios.setSenha_adotante(PasswordEncryptor.encrypt(adotanteUsuarios.getSenha_adotante()));
        }
        return adotanteUsuariosRepository.save(adotanteUsuarios);
    }

    @Override
    public Optional<AdotanteUsuarios> findById(UUID id) {
        return adotanteUsuariosRepository.findById(id);
    }

    @Override
    public Optional<AdotanteUsuarios> findByEmail(String email) {
        return adotanteUsuariosRepository.findByEmail(email);
    }

    @Override
    public List<AdotanteUsuarios> findAll() {
        return adotanteUsuariosRepository.findAll();
    }

    @Override
    public AdotanteUsuarios update(UUID id, AdotanteUsuarios updatedUser) {
        return adotanteUsuariosRepository.findById(id).map(adotanteUsuarios -> {
            adotanteUsuarios.setNome_adotante(updatedUser.getNome_adotante());
            adotanteUsuarios.setCpf_adotante(updatedUser.getCpf_adotante());
            adotanteUsuarios.setEndereco_adotante(updatedUser.getEndereco_adotante());
            adotanteUsuarios.setCelular_adotante(updatedUser.getCelular_adotante());
            adotanteUsuarios.setEmail_adotante(updatedUser.getEmail_adotante());
            adotanteUsuarios.setSenha_adotante(updatedUser.getSenha_adotante());
            adotanteUsuarios.setDescricao_outros_animais(updatedUser.getDescricao_outros_animais());
            adotanteUsuarios.setPreferencia(updatedUser.getPreferencia());

            if (updatedUser.getSenha_adotante() != null && !updatedUser.getSenha_adotante().isEmpty()) {
                adotanteUsuarios.setSenha_adotante(PasswordEncryptor.encrypt(updatedUser.getSenha_adotante()));
            }
            return adotanteUsuariosRepository.save(adotanteUsuarios);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        adotanteUsuariosRepository.deleteById(id);
    }
}
