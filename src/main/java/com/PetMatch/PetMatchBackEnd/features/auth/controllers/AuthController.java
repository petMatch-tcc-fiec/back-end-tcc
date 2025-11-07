package com.PetMatch.PetMatchBackEnd.features.auth.controllers;

import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.LoginResponse;
import com.PetMatch.PetMatchBackEnd.features.auth.dto.RegisterRequest;
import com.PetMatch.PetMatchBackEnd.features.auth.services.AuthService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Autenticação", description = "Gerencia registro e login de usuários na plataforma PetMatch.")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário com base nas informações enviadas no corpo da requisição.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody RegisterRequest request) {
        Usuario newUser = authService.register(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login de usuário",
            description = "Realiza o login de um usuário com base nas credenciais fornecidas e retorna um token JWT para autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Usuario loggedInUser = authService.login(request);
        String jwtToken = this.jwtService.generateToken(loggedInUser);
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Login de teste (ambiente de desenvolvimento)",
            description = "Realiza login automático com credenciais fixas para fins de teste.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login de teste bem-sucedido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)))
            }
    )
    @PostMapping("/loginprova")
    public ResponseEntity<LoginResponse> loginProva(@RequestBody LoginRequest request) {
        request.setEmail("usuario@exemplo.com");
        request.setPassword("Senha123!");
        Usuario loggedInUser = authService.login(request);

        String jwtToken = this.jwtService.generateToken(loggedInUser);
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
