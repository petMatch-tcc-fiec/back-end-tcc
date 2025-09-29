package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.dto.CreatedUsuarioResponseDto;
import com.PetMatch.PetMatchBackEnd.features.user.dto.RegisterAdminDto;
import com.PetMatch.PetMatchBackEnd.features.user.dto.RegisterAdotanteDto;
import com.PetMatch.PetMatchBackEnd.features.user.dto.RegisterOngDto;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
