package com.PetMatch.PetMatchBackEnd.features.user.services.impl;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.*;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdminUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdotanteUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AdminUsuariosRepository adminUsuariosRepository;
    private final AdotanteUsuariosRepository adotanteUsuariosRepository;
    private final OngUsuariosRepository ongUsuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getPassword() != null) {
            usuario.setPassword(PasswordEncryptor.encrypt(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario update(UUID id, Usuario updatedUsuario) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setName(updatedUsuario.getName());
            usuario.setAccessLevel(updatedUsuario.getAccessLevel());
            usuario.setPicture(updatedUsuario.getPicture());

            // Re-criptografa a senha apenas se uma nova for fornecida
            if (updatedUsuario.getPassword() != null && !updatedUsuario.getPassword().isEmpty()) {
                usuario.setPassword(PasswordEncryptor.encrypt(updatedUsuario.getPassword()));
            }
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public CreatedUsuarioResponseDto saveAdmin(RegisterAdminDto registerAdminDto) {
        String email = registerAdminDto.getEmail();
        if(findByEmail(email).isPresent()){
            throw new RuntimeException();
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(registerAdminDto.getEmail());
        usuario.setPassword(registerAdminDto.getPassword());
        usuario.setAccessLevel(UserLevel.ADMIN);
        Usuario savedUser = save(usuario);
        AdminUsuarios admin = new AdminUsuarios();
        admin.setUsuario(savedUser);
        admin.setCpfOuCnpjAdmin(registerAdminDto.getCpfOuCnpj());
        AdminUsuarios savedAdmin = adminUsuariosRepository.save(admin);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedAdmin.getIdAdmin()))
                .userId(String.valueOf(savedUser.getId()))
                .build();
    }

    @Override
    public CreatedUsuarioResponseDto saveAdotante(RegisterAdotanteDto registerAdotanteDto) {
        String email = registerAdotanteDto.getEmail();
        if(findByEmail(email).isPresent()) {
            throw new RuntimeException();
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(registerAdotanteDto.getEmail());
        usuario.setPassword(registerAdotanteDto.getPassword());
        usuario.setAccessLevel(UserLevel.ADOTANTE);
        Usuario savedUser = save(usuario);
        AdotanteUsuarios adotante = new AdotanteUsuarios();
        adotante.setUsuario(savedUser);
        adotante.setNomeAdotante(registerAdotanteDto.getName());
        adotante.setEmailAdotante(registerAdotanteDto.getEmail());
        adotante.setSenhaAdotante(registerAdotanteDto.getPassword());
        adotante.setCpfAdotante(registerAdotanteDto.getCpf());
        adotante.setEnderecoAdotante(registerAdotanteDto.getEndereco());
        adotante.setCelularAdotante(registerAdotanteDto.getCelular());
        adotante.setDescricaoOutrosAnimais(registerAdotanteDto.getDescricaoOutrosAnimais());
        adotante.setPreferencia(registerAdotanteDto.getPreferencia());
        AdotanteUsuarios savedAdotante = adotanteUsuariosRepository.save(adotante);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedAdotante.getIdAdotante()))
                .userId(String.valueOf(savedUser.getId()))
                .build();
    }

    @Override
    public CreatedUsuarioResponseDto saveOng(RegisterOngDto registerOngDto) {
        String email = registerOngDto.getEmail();
        if(findByEmail(email).isPresent()) {
            throw new RuntimeException();
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(registerOngDto.getEmail());
        usuario.setPassword(registerOngDto.getPassword());
        usuario.setAccessLevel(UserLevel.ONG);
        Usuario savedUser = save(usuario);
        OngUsuarios ong = new OngUsuarios();
        ong.setEnderecoOng(registerOngDto.getEndereco());
        ong.setTelefoneOng(registerOngDto.getTelefone());
        ong.setCelularOng(registerOngDto.getCelular());
        ong.setCnpjOng(registerOngDto.getCnpj());
        OngUsuarios savedOng = ongUsuariosRepository.save(ong);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedOng.getIdOng()))
                .userId(String.valueOf(savedUser.getId()))
                .build();
    }

    @Override
    public void deleteById(UUID id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public MyUserDto getMe(Usuario usuario) {
        UserLevel tipoUsuario = usuario.getAccessLevel();
        MyUserDto myUserDto = null;
        if(UserLevel.ADMIN.equals(tipoUsuario)){
            AdminUsuarios admin = adminUsuariosRepository.findByUsuario(usuario).orElseThrow();
            myUserDto = new MyUserDto();
            myUserDto.setCnpj(admin.getCpfOuCnpjAdmin());
            myUserDto.setTipo("ADMIN");
        } else if(UserLevel.ONG.equals(tipoUsuario)){
            OngUsuarios ong = ongUsuariosRepository.findByUsuario(usuario).orElseThrow();
            myUserDto = new MyUserDto();
            myUserDto.setCnpj(ong.getCnpjOng());
            myUserDto.setTipo("ONG");
        } else {
            AdotanteUsuarios adotanteUsuarios = adotanteUsuariosRepository.findByUsuario(usuario).orElseThrow();
            myUserDto = new MyUserDto();
            myUserDto.setCpf(adotanteUsuarios.getCpfAdotante());
            myUserDto.setNome(adotanteUsuarios.getNomeAdotante());
            myUserDto.setTipo("ADOTANTE");
        }
        myUserDto.setPicture(usuario.getPicture());
        myUserDto.setEmail(usuario.getEmail());
        return myUserDto;
    }
}
