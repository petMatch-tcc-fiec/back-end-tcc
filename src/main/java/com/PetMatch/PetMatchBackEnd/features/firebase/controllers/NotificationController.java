package com.PetMatch.PetMatchBackEnd.features.firebase.controllers;

import com.PetMatch.PetMatchBackEnd.features.firebase.models.dto.FcmTokenRequest;
import com.PetMatch.PetMatchBackEnd.features.firebase.models.dto.NotificationMessage;
import com.PetMatch.PetMatchBackEnd.features.firebase.services.NotificationService;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.services.UsuarioService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/notifications")
@AllArgsConstructor
@Tag(name = "Notificações", description = "Endpoints relacionados a notificações via Firebase Cloud Messaging (FCM)")
public class NotificationController {

    private final UsuarioService usuarioService;
    private final NotificationService notificationService;

    @Operation(
            summary = "Registrar ou atualizar token FCM do usuário logado",
            description = "Recebe o token FCM de um usuário autenticado e atualiza no banco de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token FCM atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
            }
    )
    @PutMapping("/token")
    public void registerFcmToken(
            Authentication authentication,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo o novo token FCM do usuário.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FcmTokenRequest.class))
            )
            FcmTokenRequest request) {

        Usuario myUser = (Usuario) authentication.getPrincipal();
        System.out.println("Recebendo novo token FCM para o usuário ID: " + myUser.getId());
        usuarioService.updateFcmToken(myUser.getId(), request);
    }

    @Operation(
            summary = "Enviar notificação a um usuário",
            description = "Envia uma notificação push via Firebase para um usuário específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notificação enviada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao enviar notificação", content = @Content)
            }
    )
    @PostMapping("/sendToUser")
    public String sendToUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo o título, corpo e ID do usuário de destino.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NotificationMessage.class))
            )
            @RequestBody NotificationMessage dto
    ) throws FirebaseMessagingException {
        return notificationService.sendNotificationToUser(dto);
    }
}
