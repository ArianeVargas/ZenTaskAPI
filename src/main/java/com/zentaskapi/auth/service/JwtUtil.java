package com.zentaskapi.auth.service;

import io.jsonwebtoken.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtUtil() throws Exception {
        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }

    // ✅ Generar token con clave privada y RS256
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(3600))) // 1 hora de duración
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // ✅ Extraer el username del token (claim 'sub')
    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // ✅ Verificar si un token ya ha expirado
    public boolean isExpired(String token) {
        try {
            Date expiration = parseToken(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true; // En caso de error, se considera expirado o inválido
        }
    }

    // ✅ Validar el token comparando el username y verificando expiración
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    // ✅ Parsear el token usando la clave pública
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);
    }

    // ✅ Leer clave privada desde archivo PEM
    private PrivateKey loadPrivateKey() throws Exception {
        InputStream input = new ClassPathResource("jwt/private.key").getInputStream();
        byte[] keyBytes = input.readAllBytes();
        String privateKeyPEM = new String(keyBytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // ✅ Leer clave pública desde archivo PEM
    private PublicKey loadPublicKey() throws Exception {
        InputStream input = new ClassPathResource("jwt/public.key").getInputStream();
        byte[] keyBytes = input.readAllBytes();
        String publicKeyPEM = new String(keyBytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}