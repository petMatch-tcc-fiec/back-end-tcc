package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.utils.ImageUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

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

    @GetMapping("/me")
    public MyUserDto getMe(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuarioService.getMe(usuario);
    }

    @PutMapping("/photo")
    public void insertUserImage(@RequestParam("image") MultipartFile image, Authentication authentication){
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String imageName = ImageUtils.saveImage(image);
        usuario.setPicture(imageName);
        usuarioService.save(usuario);
    }
}
