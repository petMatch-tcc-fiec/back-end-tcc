package com.PetMatch.PetMatchBackEnd.features.auth.controllers;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginResponse;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestAdotante;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequestOng;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.AdotanteUsuarios;
import com.PetMatch.PetMatchBackEnd.features.user.models.Ong.OngUsuarios;
import com.PetMatch.PetMatchBackEnd.utils.JwtService;
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
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AdotanteUsuarios> register(@RequestBody RegisterRequestAdotante request) {
        AdotanteUsuarios newUser = authService.register(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/registerOng")
    public ResponseEntity<OngUsuarios> registerOng(@RequestBody RegisterRequestOng request) {
        OngUsuarios newOng = authService.registerOng(request);
        return new ResponseEntity<>(newOng, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AdotanteUsuarios loggedInUser = authService.login(request);
        String jwtToken = this.jwtService.generateToken(loggedInUser);
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}