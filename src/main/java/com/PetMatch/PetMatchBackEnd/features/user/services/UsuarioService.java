package com.PetMatch.PetMatchBackEnd.features.user.services;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService{

    Usuario save(Usuario usuario);
    Optional<Usuario> findById(UUID id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
    Usuario update(UUID id, Usuario updatedUsuario);
    CreatedUsuarioResponseDto saveAdmin(RegisterAdminDto registerAdminDto);
    CreatedUsuarioResponseDto saveAdotante(RegisterAdotanteDto registerAdotanteDto);
    CreatedUsuarioResponseDto saveOng(RegisterOngDto registerOngDto);
    void deleteById(UUID id);
    MyUserDto getMe(Usuario usuario);
}
