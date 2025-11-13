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

    /**
     * Registro de novo usuário.
     * AGORA RETORNA OS DADOS COMPLETOS DO USUÁRIO + TOKEN.
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        // 1. Cria o usuário
        Usuario newUser = authService.register(request);

        // 2. Gera o token
        String jwtToken = jwtService.generateToken(newUser);

        // --- 3. CORREÇÃO: Monta a resposta COMPLETA ---
        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .id(newUser.getId()) // O UUID que o frontend precisa
                .email(newUser.getEmail())
                .nome(newUser.getName()) // (Confirme se o método é .getName() na sua entidade Usuario)
                .tipo(newUser.getAccessLevel().toString()) // (Ex: "ADOTANTE")
                .build();
        // ---------------------------------------------

        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    /**
     * Login tradicional.
     * AGORA RETORNA OS DADOS COMPLETOS DO USUÁRIO + TOKEN.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. O service (corrigido) acha o usuário
        Usuario loggedInUser = authService.login(request);

        // 2. Gera o token
        String jwtToken = this.jwtService.generateTokenComplete(loggedInUser);

        // --- 3. CORREÇÃO: Monta a resposta COMPLETA ---
        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .id(loggedInUser.getId()) // O UUID que o frontend precisa
                .email(loggedInUser.getEmail())
                .nome(loggedInUser.getName()) // (Confirme se o método é .getName())
                .tipo(loggedInUser.getAccessLevel().toString()) // (Ex: "ONG")
                .build();
        // ---------------------------------------------;

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

        // --- 3. CORREÇÃO APLICADA AQUI TAMBÉM ---
        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .id(loggedInUser.getId())
                .email(loggedInUser.getEmail())
                .nome(loggedInUser.getName())
                .tipo(loggedInUser.getAccessLevel().toString())
                .build();
        // -----------------------------------------

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
