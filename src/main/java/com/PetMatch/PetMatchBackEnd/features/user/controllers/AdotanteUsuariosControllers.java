package com.PetMatch.PetMatchBackEnd.features.user.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/adotantes")
public class AdotanteUsuariosControllers {

    @GetMapping
    public void test() {
        System.out.println("Teste ok");
    }
}
