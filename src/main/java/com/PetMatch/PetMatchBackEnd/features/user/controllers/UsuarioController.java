package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.dto.*;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.OngUsuarios;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
            summary = "Atualizar perfil de Adotante",
            description = "Atualiza os dados de perfil de um Adotante, incluindo informações pessoais.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil de Adotante atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário ou Adotante não encontrado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso Negado (Token não corresponde ao ID)", content = @Content)
            }
    )
    @PutMapping("/adotante/{userId}")
    public ResponseEntity<AdotanteUsuarios> updateAdotante(
            @Parameter(description = "ID do usuário (UUID) a ser atualizado", required = true)
            @PathVariable UUID userId,
            @Valid @RequestBody RegisterAdotanteDto updateAdotanteDto,
            Authentication authentication) {

        // Nota de Segurança: Garante que o usuário autenticado só possa editar o seu próprio perfil.
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        if (!usuarioAutenticado.getId().equals(userId)) {
            // Retorna 403 Forbidden se o usuário autenticado tentar editar outro ID
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AdotanteUsuarios updatedAdotante = usuarioService.updateAdotante(userId, updateAdotanteDto);
        return ResponseEntity.ok(updatedAdotante);
    }

    @Operation(
            summary = "Atualizar perfil de ONG",
            description = "Atualiza os dados de perfil de uma ONG, incluindo informações de contato e endereço.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil de ONG atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário ou ONG não encontrada", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acesso Negado (Token não corresponde ao ID)", content = @Content)
            }
    )
    @PutMapping("/ong/{userId}")
    public ResponseEntity<OngUsuarios> updateOng(
            @Parameter(description = "ID do usuário (UUID) a ser atualizado", required = true)
            @PathVariable UUID userId,
            @Valid @RequestBody RegisterOngDto updateOngDto,
            Authentication authentication) {

        // Nota de Segurança: Garante que o usuário autenticado só possa editar o seu próprio perfil.
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        if (!usuarioAutenticado.getId().equals(userId)) {
            // Retorna 403 Forbidden se o usuário autenticado tentar editar outro ID
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        OngUsuarios updatedOng = usuarioService.updateOng(userId, updateOngDto);
        return ResponseEntity.ok(updatedOng);
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
