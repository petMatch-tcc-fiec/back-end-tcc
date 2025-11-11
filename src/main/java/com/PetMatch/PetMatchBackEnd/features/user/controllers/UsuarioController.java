package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.RegisterState;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.PetMatch.PetMatchBackEnd.shared.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api/usuarios")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários — criação, autenticação e dados do perfil.")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final S3Service s3Service;

    @Operation(
            summary = "Registrar novo administrador",
            description = "Cria um novo usuário com permissão de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping("/admin")
    public CreatedUsuarioResponseDto registerAdmin(
            @Valid @RequestBody RegisterAdminDto registerAdminDto) throws Exception {
        return usuarioService.saveAdmin(registerAdminDto);
    }

    @Operation(
            summary = "Registrar novo adotante",
            description = "Cria um novo usuário com perfil de Adotante (usuário comum).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Adotante criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping("/adotante")
    public CreatedUsuarioResponseDto registerAdotante(
            @Valid @RequestBody RegisterAdotanteDto registerAdotanteDto) {
        return usuarioService.saveAdotante(registerAdotanteDto);
    }

    @Operation(
            summary = "Registrar nova ONG",
            description = "Cria um novo usuário com perfil de ONG (Organização).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "ONG criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping("/ong")
    public CreatedUsuarioResponseDto registerOng(
            @Valid @RequestBody RegisterOngDto registerOngDto) {
        return usuarioService.saveOng(registerOngDto);
    }

    @Operation(
            summary = "Obter dados do usuário autenticado",
            description = "Retorna as informações do usuário autenticado através do token JWT.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
            }
    )
    @GetMapping("/me")
    public MyUserDto getMe(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuarioService.getMe(usuario);
    }

    @Operation(
            summary = "Atualizar foto do usuário",
            description = "Faz upload da imagem de perfil do usuário autenticado e salva no S3.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagem atualizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro no upload da imagem", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
            }
    )
    @PutMapping("/photo")
    public void insertUserImage(
            @Parameter(description = "Arquivo de imagem do usuário", required = true)
            @RequestParam("image") MultipartFile image,
            Authentication authentication) throws IOException {

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String imageName = s3Service.uploadFile(image);
        usuario.setPicture(imageName);
        usuario.setState(RegisterState.IMAGE_CREATED);
        usuarioService.save(usuario);
    }
}
