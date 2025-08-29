package com.PetMatch.PetMatchBackEnd.features.user.services.impl;

import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdotanteUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.services.AdotanteUsuariosService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdotanteUsuariosServiceImpl implements AdotanteUsuariosService, UserDetailsService {

    private final AdotanteUsuariosRepository adotanteUsuariosRepository;

    public AdotanteUsuariosServiceImpl(AdotanteUsuariosRepository adotanteUsuariosRepository) {
        this.adotanteUsuariosRepository = adotanteUsuariosRepository;
    }

    @Override
    public AdotanteUsuarios save(AdotanteUsuarios adotanteUsuarios) {
        if (adotanteUsuarios.getSenhaAdotante() != null) {
            adotanteUsuarios.setSenhaAdotante(PasswordEncryptor.encrypt(adotanteUsuarios.getSenhaAdotante()));
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
            adotanteUsuarios.setNomeAdotante(updatedUser.getNomeAdotante());
            adotanteUsuarios.setCpfAdotante(updatedUser.getCpfAdotante());
            adotanteUsuarios.setEnderecoAdotante(updatedUser.getEnderecoAdotante());
            adotanteUsuarios.setCelularAdotante(updatedUser.getCelularAdotante());
            adotanteUsuarios.setEmail(updatedUser.getEmail());
            adotanteUsuarios.setSenhaAdotante(updatedUser.getSenhaAdotante());
            adotanteUsuarios.setDescricaoOutrosAnimais(updatedUser.getDescricaoOutrosAnimais());
            adotanteUsuarios.setPreferencia(updatedUser.getPreferencia());

            if (updatedUser.getSenhaAdotante() != null && !updatedUser.getSenhaAdotante().isEmpty()) {
                adotanteUsuarios.setSenhaAdotante(PasswordEncryptor.encrypt(updatedUser.getSenhaAdotante()));
            }
            return adotanteUsuariosRepository.save(adotanteUsuarios);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        adotanteUsuariosRepository.deleteById(id);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adotanteUsuariosRepository.findByEmail(email).orElseThrow();
    }
}
