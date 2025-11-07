package com.PetMatch.PetMatchBackEnd.features.user.services.impl;

import com.PetMatch.PetMatchBackEnd.features.firebase.models.dto.FcmTokenRequest;
import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.*;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdminUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.AdotanteUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.OngUsuariosRepository;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.utils.PasswordEncryptor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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
        usuario.setPassword(PasswordEncryptor.encrypt(registerAdminDto.getPassword()));
        usuario.setAccessLevel(UserLevel.ADMIN);
        Usuario savedUser = save(usuario);
        AdminUsuarios admin = new AdminUsuarios();
        admin.setUsuario(savedUser);
        admin.setNomeAdmin(registerAdminDto.getName());
        admin.setEmailAdmin(registerAdminDto.getEmail());
        admin.setSenhaAdmin(PasswordEncryptor.encrypt(registerAdminDto.getPassword()));
        admin.setCpfOuCnpjAdmin(registerAdminDto.getCpfOuCnpj());
        AdminUsuarios savedAdmin = adminUsuariosRepository.save(admin);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedAdmin.getId()))
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
        usuario.setPassword(PasswordEncryptor.encrypt(registerAdotanteDto.getPassword()));
        usuario.setAccessLevel(UserLevel.ADOTANTE);
        Usuario savedUser = save(usuario);
        AdotanteUsuarios adotante = new AdotanteUsuarios();
        adotante.setUsuario(savedUser);
        adotante.setNomeAdotante(registerAdotanteDto.getName());
        adotante.setEmailAdotante(registerAdotanteDto.getEmail());
        adotante.setSenhaAdotante(PasswordEncryptor.encrypt(registerAdotanteDto.getPassword()));
        adotante.setCpfAdotante(registerAdotanteDto.getCpf());
        adotante.setEnderecoAdotante(registerAdotanteDto.getEndereco());
        adotante.setCelularAdotante(registerAdotanteDto.getCelular());
        adotante.setDescricaoOutrosAnimais(registerAdotanteDto.getDescricaoOutrosAnimais());
        adotante.setPreferencia(registerAdotanteDto.getPreferencia());
        AdotanteUsuarios savedAdotante = adotanteUsuariosRepository.save(adotante);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedAdotante.getId()))
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
        usuario.setPassword(PasswordEncryptor.encrypt(registerOngDto.getPassword()));
        usuario.setAccessLevel(UserLevel.ONG);
        Usuario savedUser = save(usuario);
        OngUsuarios ong = new OngUsuarios();
        ong.setUsuario(savedUser);
        ong.setNomeFantasiaOng(registerOngDto.getName());
        ong.setEmailOng(registerOngDto.getEmail());
        ong.setSenhaOng(PasswordEncryptor.encrypt(registerOngDto.getPassword()));
        ong.setEnderecoOng(registerOngDto.getEndereco());
        ong.setTelefoneOng(registerOngDto.getTelefone());
        ong.setCelularOng(registerOngDto.getCelular());
        ong.setCnpjOng(registerOngDto.getCnpj());
        OngUsuarios savedOng = ongUsuariosRepository.save(ong);
        return CreatedUsuarioResponseDto.builder()
                .id(String.valueOf(savedOng.getId()))
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

    public Usuario updateFcmToken(UUID userId, FcmTokenRequest request) {

        // 1. Busca o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        System.out.println(userId);
        // 2. Atualiza o atributo fcmToken
        usuario.setFcmToken(request.getFcmToken());

        // 3. Salva a alteração (o @Transactional garante que a persistência ocorra)
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void createUsers(InputStream inputStream) {
        List<UsuarioCsvRepresentation> users = new ArrayList<>();
        try (Reader reader = new InputStreamReader(inputStream)) {

            // Create a CsvToBean object from the Reader
            CsvToBean<UsuarioCsvRepresentation> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(UsuarioCsvRepresentation.class) // Specify the target bean class
                    .withIgnoreLeadingWhiteSpace(true) // Clean up any extra spaces
                    .withSkipLines(0) // Skips the header row if present
                    .build();

            // Parse the data and return a List of beans
            users = csvToBean.parse();
        } catch (Exception e) {
            // Handle IO or CSV parsing exceptions
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }

        try {

            for (UsuarioCsvRepresentation csvUser : users) {
                Usuario usuario = new Usuario();
                usuario.setEmail(csvUser.getEmail());
                usuario.setName(csvUser.getName());
                usuario.setPassword(PasswordEncryptor.encrypt(csvUser.getPassword()));
                UserLevel level = UserLevel.valueOf(csvUser.getAccessLevel());
                usuario.setAccessLevel(level);
                save(usuario);
                switch (level) {
                    case UserLevel.ADOTANTE:
                        AdotanteUsuarios adotante = new AdotanteUsuarios();

                        adotante.setUsuario(usuario);
                        adotante.setCpfAdotante(csvUser.getCpfAdotante());
                        adotante.setEnderecoAdotante(csvUser.getEnderecoAdotante());
                        adotante.setCelularAdotante(csvUser.getCelularAdotante());
                        adotante.setDescricaoOutrosAnimais(csvUser.getDescricaoOutrosAnimais());
                        adotante.setPreferencia(csvUser.getPreferencia());
                        adotanteUsuariosRepository.save(adotante);
                        break;
                    case UserLevel.ADMIN:
                        AdminUsuarios admin = new AdminUsuarios();

                        admin.setUsuario(usuario);
                        admin.setCpfOuCnpjAdmin(csvUser.getCnpjOuCnpjAdmin());
                        adminUsuariosRepository.save(admin);
                        break;

                    case UserLevel.ONG:
                        OngUsuarios ong = new OngUsuarios();
                        ong.setUsuario(usuario);
                        ong.setEnderecoOng(csvUser.getEnderecoOng());
                        ong.setTelefoneOng(csvUser.getTelefoneOng());
                        ong.setCelularOng(csvUser.getCelularOng());
                        ong.setCnpjOng(csvUser.getCnpjOng());
                        ongUsuariosRepository.save(ong);

                        break;
                    default:
                        break;

                }

            }
        }catch (Exception ex){
            throw new RuntimeException("Failed to parse CSV file: " + ex.getMessage(), ex);

        }
    }
}
