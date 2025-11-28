package com.PetMatch.PetMatchBackEnd.features.animais.controllers;

import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.FichaMedicaDTO;
import com.PetMatch.PetMatchBackEnd.features.animais.services.FichaMedicaService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/v1/api/animais")
@RequiredArgsConstructor
@Tag(name = "Ficha Médica", description = "Endpoints para gerenciar vacinas e histórico de saúde.")
public class FichaMedicaController {

    private final FichaMedicaService fichaMedicaService;

    @Operation(summary = "Salvar ou Atualizar Ficha Médica", description = "Cria ou atualiza histórico. Apenas a ONG dona do animal pode fazer isso.")
    @PostMapping("/{idAnimal}/ficha")
    public ResponseEntity<FichaMedicaDTO> salvarFicha(
            @PathVariable UUID idAnimal,
            @RequestBody FichaMedicaDTO dto,
            @AuthenticationPrincipal Usuario usuario) {

        // Chama o seu Service que você acabou de corrigir
        FichaMedicaDTO salvo = fichaMedicaService.salvarOuAtualizar(idAnimal, dto, usuario.getId());

        return ResponseEntity.ok(salvo);
    }

    @Operation(summary = "Buscar Ficha Médica", description = "Retorna os dados médicos do animal.")
    @GetMapping("/{idAnimal}/ficha")
    public ResponseEntity<FichaMedicaDTO> buscarFicha(@PathVariable UUID idAnimal) {

        FichaMedicaDTO ficha = fichaMedicaService.buscarPorAnimal(idAnimal);

        if (ficha == null) {
            return ResponseEntity.noContent().build(); // Retorna vazio se não tiver ficha
        }
        return ResponseEntity.ok(ficha);
    }
}
