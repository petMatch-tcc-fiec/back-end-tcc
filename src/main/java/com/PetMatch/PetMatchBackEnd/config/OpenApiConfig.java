package com.PetMatch.PetMatchBackEnd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI petMatchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PetMatch API")
                        .description("API para sistema de adoção de animais")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PetMatch Team")
                                .email("contato@petmatch.com")
                                .url("https://petmatch.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:5000")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.petmatch.com")
                                .description("Servidor de Produção")))
                // Configuração de autenticação JWT
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT no formato: Bearer {token}")));
    }
}