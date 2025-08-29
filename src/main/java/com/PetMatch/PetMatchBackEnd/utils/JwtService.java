package com.PetMatch.PetMatchBackEnd.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // A chave secreta deve ser gerada e armazenada de forma segura.
    // **Não hardcode uma chave como esta em produção!**
    private final SecretKey secretKey;
    private final long expirationTimeMs;

    public JwtService(@Value("${jwt.secret}") String secretString,
                      @Value("${jwt.expiration}") long expirationTimeMs) {
        // A chave deve ser decodificada de Base64 para ser usada
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTimeMs = expirationTimeMs;
    }

    public String generateToken(UserDetails userDetails) {
        // Extrai as autoridades (papéis/permissões) do UserDetails
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Cria o mapa de claims com as autoridades
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        // Chama o método que realmente constrói o token com o novo claims
        return generateToken(userDetails.getUsername(), claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Extrai o nome de usuário do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai a data de expiração do token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Verifica se o token expirou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrai um "claim" específico do token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Extrai todos os "claims" do token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gera um token JWT com base nos detalhes do usuário e nos claims extras.
     *
     * @param subject    O assunto do token, geralmente um identificador do usuário (e.g., username).
     * @param claims     Um mapa de claims (informações) adicionais para incluir no payload do JWT.
     * @return O token JWT assinado como uma String.
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        // Data de expiração do token
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeMs);

        // Constrói o token JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
