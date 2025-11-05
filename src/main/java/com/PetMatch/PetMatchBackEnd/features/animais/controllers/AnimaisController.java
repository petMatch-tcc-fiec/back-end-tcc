package com.PetMatch.PetMatchBackEnd.features.animais.controllers;

import com.PetMatch.PetMatchBackEnd.features.animais.models.Animais;
import com.PetMatch.PetMatchBackEnd.features.animais.models.dtos.AnimalSearch;
import com.PetMatch.PetMatchBackEnd.features.animais.services.AnimaisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/animais")
public class AnimaisController {

    private final AnimaisService animaisService;

    public AnimaisController(AnimaisService animaisService) {
        this.animaisService = animaisService;
    }

    // POST - Criar novo animal
    @PostMapping
    public ResponseEntity<Animais> createAnimal(@RequestBody Animais animais) {
        Animais novoAnimal = animaisService.create(animais);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAnimal);
    }

    // GET - Buscar todos os animais
    @GetMapping
    public ResponseEntity<List<Animais>> getAllAnimais() {
        List<Animais> animais = animaisService.findAll();
        return ResponseEntity.ok(animais);
    }

    // GET - Buscar animal por ID
    @GetMapping("/{id}")
    public ResponseEntity<Animais> getAnimalById(@PathVariable UUID id) {
        return animaisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - Atualizar animal
    @PutMapping("/{id}")
    public ResponseEntity<Animais> updateAnimal(
            @PathVariable UUID id,
            @RequestBody Animais animais) {
        return animaisService.update(id, animais)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Deletar animal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable UUID id) {
        if (animaisService.findById(id).isPresent()) {
            animaisService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/filters/all")
    public List<Animais> getAnimalById(AnimalSearch animalSearch){
        return animaisService.findAllWithQueries(animalSearch);
    }
}
