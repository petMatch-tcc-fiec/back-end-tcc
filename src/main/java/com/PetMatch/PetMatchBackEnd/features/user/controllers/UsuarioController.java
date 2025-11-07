package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.RegisterState;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.shared.service.S3Service;
import com.PetMatch.PetMatchBackEnd.utils.ImageUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final S3Service s3Service;

    @PostMapping("/admin")
    public CreatedUsuarioResponseDto registerAdmin(@Valid @RequestBody RegisterAdminDto registerAdminDto) throws Exception {
        return usuarioService.saveAdmin(registerAdminDto);
    }

    @PostMapping("/adotante")
    public CreatedUsuarioResponseDto registerAdotante(@Valid @RequestBody RegisterAdotanteDto registerAdotanteDto){
        return usuarioService.saveAdotante(registerAdotanteDto);
    }

    @PostMapping("/ong")
    public CreatedUsuarioResponseDto registerOng(@Valid @RequestBody RegisterOngDto registerOngDto){
        return usuarioService.saveOng(registerOngDto);
    }

    public void createUsers(@RequestParam("inputFile") MultipartFile file) throws IOException {
        usuarioService.createUsers(file.getInputStream());
    }

    @GetMapping("/me")
    public MyUserDto getMe(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuarioService.getMe(usuario);
    }

    @PutMapping("/photo")
    public void insertUserImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        //String imageName = ImageUtils.saveImage(image);
        String imageName = s3Service.uploadFile(image);
        usuario.setPicture(imageName);
        usuario.setState(RegisterState.IMAGE_CREATED);
        usuarioService.save(usuario);
    }
}
