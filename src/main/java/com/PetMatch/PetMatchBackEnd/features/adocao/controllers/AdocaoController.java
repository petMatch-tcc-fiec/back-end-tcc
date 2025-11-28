package com.PetMatch.PetMatchBackEnd.features.adocao.controllers;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.services.AdocaoService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/adocao")
@Tag(name = "Adoção", description = "Gerencia o processo de adoção de animais")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    // ----------------------------------------------------------------
    // ✨ NOVO ENDPOINT: Lista os interesses do PRÓPRIO USUÁRIO LOGADO
    // Rota final: GET /v1/api/adocao/adotante
    // ----------------------------------------------------------------
    @Operation(
            summary = "Listar meus interesses de adoção",
            description = "Retorna a lista de animais que o usuário logado demonstrou interesse."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de interesses do usuário retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InteresseResponseDTO.class))
    )
    @GetMapping("/adotante")
    public ResponseEntity<List<InteresseResponseDTO>> getMeusInteresses(
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        // Chama o método que criamos no passo anterior no Service
        List<InteresseResponseDTO> lista = adocaoService.listarMeusInteresses(usuarioLogado);
        return ResponseEntity.ok(lista);
    }

    // ----------------------------------------------------------------
    // REGISTRAR INTERESSE (Já existia)
    // ----------------------------------------------------------------
    @Operation(
            summary = "Registrar interesse em um animal",
            description = "Permite que um usuário registrado demonstre interesse em adotar um animal específico."
    )
    @ApiResponse(responseCode = "200", description = "Interesse registrado com sucesso")
    @PostMapping("/animal/{animalId}/match")
    public ResponseEntity<Void> registrarInteresse(
            @Parameter(description = "UUID do animal para o qual o interesse será registrado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID animalId,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        adocaoService.registrarInteresse(animalId, usuarioLogado.getId());
        return ResponseEntity.ok().build();
    }

    // ----------------------------------------------------------------
    // LISTAR FILA DE ESPERA DO ANIMAL (Visão da ONG - Já existia)
    // ----------------------------------------------------------------
    @Operation(
            summary = "Listar interessados por animal",
            description = "Retorna a lista de usuários interessados em adotar um determinado animal."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de interessados retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InteresseResponseDTO.class))
    )
    @GetMapping("/animal/{animalId}/lista-espera")
    public ResponseEntity<List<InteresseResponseDTO>> getListaDeEspera(
            @Parameter(description = "UUID do animal", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID animalId
    ) {
        List<InteresseResponseDTO> lista = adocaoService.listarInteressadosPorAnimal(animalId);
        return ResponseEntity.ok(lista);
    }

    // ----------------------------------------------------------------
    // AVALIAR INTERESSE (Aprovar/Reprovar - Já existia)
    // ----------------------------------------------------------------
    @Operation(
            summary = "Avaliar interesse de adoção",
            description = "Permite que o administrador avalie um interesse de adoção e defina o status (por exemplo, APROVADO, REJEITADO)."
    )
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @PutMapping("/interesse/{interesseId}/avaliar")
    public ResponseEntity<Void> avaliarInteresse(
            @Parameter(description = "UUID do interesse a ser avaliado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID interesseId,
            @RequestBody
            @Parameter(description = "Mapa contendo o novo status de adoção. Exemplo: { \"status\": \"APROVADO\" }")
            Map<String, String> request
    ) {
        String statusString = request.get("status");
        AdocaoStatus novoStatus = AdocaoStatus.valueOf(statusString.toUpperCase());
        adocaoService.avaliarInteresse(interesseId, novoStatus);
        return ResponseEntity.ok().build();
    }
}