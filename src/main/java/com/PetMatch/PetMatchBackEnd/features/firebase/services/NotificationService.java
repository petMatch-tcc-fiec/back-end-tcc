package com.PetMatch.PetMatchBackEnd.features.firebase.services;

import com.PetMatch.PetMatchBackEnd.features.firebase.models.dto.NotificationMessage;
import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import com.PetMatch.PetMatchBackEnd.features.user.repositories.UsuarioRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(com.PetMatch.PetMatchBackEnd.features.firebase.services.NotificationService.class);

    private final UsuarioRepository usuarioRepository;
    private final FirebaseMessaging firebaseMessaging;

    // Injetamos o Repositório e o FirebaseApp para obter a instância do FirebaseMessaging
    public NotificationService(UsuarioRepository usuarioRepository, FirebaseApp firebaseApp) {
        this.usuarioRepository = usuarioRepository;
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    /**
     * Envia uma notificação para o token FCM de um usuário específico.
     * @param dto O DTO contendo o userId, title e message.
     * @return O ID da mensagem retornada pelo Firebase.
     * @throws RuntimeException Se o usuário ou o token FCM não for encontrado.
     * @throws FirebaseMessagingException Se houver erro de envio pelo Firebase.
     */
    public String sendNotificationToUser(NotificationMessage dto) throws FirebaseMessagingException {

        // 1. Busca o usuário e seu token FCM
        Optional<Usuario> userOptional = usuarioRepository.findById(UUID.fromString(dto.getAuthUserId()));

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuário com ID " + dto.getAuthUserId() + " não encontrado.");
        }

        String fcmToken = userOptional.get().getFcmToken();

        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            throw new RuntimeException("Token FCM não encontrado para o usuário ID: " + dto.getAuthUserId());
        }

        // 2. Constrói o objeto Notification (para exibição na tela)
        Notification notification = Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getMessage())
                .build();

        // 3. Constrói o objeto Message do Firebase, usando o token específico
        Message message = Message.builder()
                .setToken(fcmToken) // Define o token do dispositivo de destino
                .setNotification(notification)
                // Opcional: Adiciona dados customizados para o cliente consumir
                .putData("custom_id", "12345")
                .build();

        // 4. Envia a mensagem e retorna o ID da resposta
        String response = firebaseMessaging.send(message);

        System.out.println("Notificação enviada com sucesso. ID da mensagem Firebase: " + response);

        return response;
    }

    /**
     * --- MÉTODO NOVO (O QUE EU CRIEI) ---
     * Envia uma notificação push direta para um token.
     * Usado por outros serviços (como AdocaoService) que já têm o token e não querem lançar exceção.
     * @param token O token FCM do dispositivo.
     * @param title O título da notificação.
     * @param body O corpo da mensagem.
     */
    public void sendPushNotification(String token, String title, String body) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token) // O FCM Token do dispositivo do usuário
                    .setNotification(notification)
                    .build();

            String response = firebaseMessaging.send(message);
            log.info("Notificação direta enviada com sucesso: " + response);

        } catch (Exception e) {
            // Este método apenas loga o erro e não lança exceção,
            // para não quebrar a transação principal (ex: aprovar a adoção).
            log.error("Erro ao enviar notificação push direta para o token {}: {}", token, e.getMessage());
        }
    }
}