package com.PetMatch.PetMatchBackEnd.features.user.admin.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/admin")
@Tag(name = "Admin", description = "Endpoints administrativos protegidos por autenticação e permissão de ADMIN.")
public class AdminController {

    @Operation(
            summary = "Testar endpoint administrativo",
            description = "Endpoint protegido que valida se o usuário autenticado possui a autoridade ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Acesso permitido. O usuário é um ADMIN."),
                    @ApiResponse(responseCode = "403", description = "Acesso negado. O usuário não possui permissão ADMIN."),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido.")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test")
    public void testRequest(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        System.out.println(usuario);
    }
}
