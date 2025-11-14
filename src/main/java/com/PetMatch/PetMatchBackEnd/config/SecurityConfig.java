package com.PetMatch.PetMatchBackEnd.config;

import com.PetMatch.PetMatchBackEnd.config.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //mudei

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Define o SecurityFilterChain que configura as regras de segurança HTTP.
     *
     * @param http o objeto HttpSecurity para configurar a segurança web
     * @return o SecurityFilterChain configurado
     * @throws Exception se ocorrer um erro durante a configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita a proteção CSRF
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
                })
                .authenticationProvider(authenticationProvider)

                .authorizeHttpRequests(auth -> auth

                        // ------------------------------------------------------------
                        // 1️⃣ ROTAS PÚBLICAS (sem necessidade de login)
                        // ------------------------------------------------------------
                        .requestMatchers(HttpMethod.POST, "/v1/api/usuarios/adotante").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/api/usuarios/admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/api/usuarios/ong").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/api/auth/**").permitAll()
                        .requestMatchers("/images/**", "/v1/api/auth/**", "/v1/api/notifications/sendToUser").permitAll()

                        // ------------------------------------------------------------
                        // 2️⃣ ROTAS RESTRITAS POR CARGO / AUTORIDADE (SÓ ONG)
                        // ------------------------------------------------------------
                        .requestMatchers("/v1/api/admin/**").hasAuthority("ADMIN")

                        // --- Regras de Adocao (SÓ ONG) ---
                        .requestMatchers(
                                // "/v1/api/animais/**", // <-- REMOVIDO DAQUI
                                "/v1/api/adocao/animal/lista-espera/**",
                                "/v1/api/adocao/interesse/avaliar/**"
                        ).hasAuthority("ONG")

                        // --- Regras de Eventos (SÓ ONG) ---
                        .requestMatchers(HttpMethod.POST, "/v1/api/eventos").hasAuthority("ONG")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/eventos/**").hasAuthority("ONG")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/eventos/**").hasAuthority("ONG")

                        // --- Regras de Animais (SÓ ONG) ---
                        .requestMatchers(HttpMethod.POST, "/v1/api/animais/**").hasAuthority("ONG")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/animais/**").hasAuthority("ONG")
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/animais/**").hasAuthority("ONG")


                        // ------------------------------------------------------------
                        // 3️⃣ ROTAS QUE EXIGEM APENAS AUTENTICAÇÃO (qualquer usuário logado)
                        // ------------------------------------------------------------

                        // --- Regras de Visualização (QUALQUER UM LOGADO) ---
                        .requestMatchers(HttpMethod.GET, "/v1/api/eventos", "/v1/api/eventos/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/api/animais", "/v1/api/animais/**").authenticated() // <-- ADICIONADO

                        .requestMatchers(HttpMethod.PUT, "/v1/api/notifications/token").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/api/adocao/animal/match/**").authenticated()

                        // ------------------------------------------------------------
                        // 4️⃣ QUALQUER OUTRA ROTA EXIGE AUTENTICAÇÃO
                        // ------------------------------------------------------------
                        .anyRequest().authenticated()
                )
// ... (resto do seu método) ...
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
//configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
