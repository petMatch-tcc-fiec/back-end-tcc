package com.PetMatch.PetMatchBackEnd.features.auth.controllers;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AdotanteUsuarios> register(@RequestBody RegisterRequest request) {
        AdotanteUsuarios newUser = authService.register(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AdotanteUsuarios> login(@RequestBody LoginRequest request) {
        AdotanteUsuarios loggedInUser = authService.login(request);
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }
}