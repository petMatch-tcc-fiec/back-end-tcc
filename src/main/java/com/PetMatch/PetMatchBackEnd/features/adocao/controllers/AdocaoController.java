package com.PetMatch.PetMatchBackEnd.features.adocao.controllers;

import com.PetMatch.PetMatchBackEnd.features.adocao.dtos.InteresseResponseDTO;
import com.PetMatch.PetMatchBackEnd.features.adocao.enums.AdocaoStatus;
import com.PetMatch.PetMatchBackEnd.features.adocao.services.AdocaoService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/adocao")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping("/animal/{animalId}/match")
    public ResponseEntity<Void> registrarInteresse(@PathVariable UUID animalId, @AuthenticationPrincipal Usuario usuarioLogado) { // PathVariable é UUID
        adocaoService.registrarInteresse(animalId, usuarioLogado.getId()); // O getId() do User agora retorna UUID
        return ResponseEntity.ok().build();
    }

    @GetMapping("/animal/{animalId}/lista-espera")
    public ResponseEntity<List<InteresseResponseDTO>> getListaDeEspera(@PathVariable UUID animalId) { // PathVariable é UUID
        List<InteresseResponseDTO> lista = adocaoService.listarInteressadosPorAnimal(animalId);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/interesse/{interesseId}/avaliar")
    public ResponseEntity<Void> avaliarInteresse(@PathVariable UUID interesseId, @RequestBody Map<String, String> request) { // PathVariable é UUID
        String statusString = request.get("status");
        AdocaoStatus novoStatus = AdocaoStatus.valueOf(statusString.toUpperCase());
        adocaoService.avaliarInteresse(interesseId, novoStatus);
        return ResponseEntity.ok().build();
    }
}
