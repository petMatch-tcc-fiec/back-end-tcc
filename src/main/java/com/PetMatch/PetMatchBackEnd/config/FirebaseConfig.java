package com.PetMatch.PetMatchBackEnd.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    // Injeta a localização do arquivo de credenciais
    // O valor 'classpath:serviceAccountKey.json' pressupõe que o arquivo
    // está em src/main/resources/serviceAccountKey.json
    @Value("classpath:firebase-service-account.json")
    Resource serviceAccountResource;

    /**
     * Configura e retorna a instância única do FirebaseApp.
     * @return FirebaseApp inicializado.
     * @throws IOException se o arquivo de credenciais não puder ser lido.
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {

            // Tenta obter o InputStream do arquivo JSON
            InputStream serviceAccount = serviceAccountResource.getInputStream();

            // Constrói as opções de inicialização do Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    //.setDatabaseUrl(databaseUrl)
                    // Outras opções como setStorageBucket() podem ser adicionadas aqui
                    .build();

            // Inicializa o FirebaseApp
            FirebaseApp app = FirebaseApp.initializeApp(options);

            System.out.println("FirebaseApp inicializado com sucesso para o ID: " + app.getName());
            return app;
        }

        // Se já estiver inicializado (em casos de auto-reload, etc.), retorna a instância padrão
        return FirebaseApp.getInstance();
    }
}

