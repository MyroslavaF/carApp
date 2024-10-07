package com.mfarion.carregistry.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    //el valor de la clave secreta del JWT desde el archivo de configuración
    @Value("${token.secret.key}")
    public
    String jwtSecretKey;
    // el valor del tiempo de expiración del JWT desde el archivo de configuración
    @Value("${token.expirationms}")
    public
    Long jwtExpirationMs;


    //recuperar username a partir de un token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);//extrae el claim sub (subject) del token, que en este caso es el nombre de usuario
    }

    //Verifica si el token es válido para el usuario proporcionado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //recuperar todos los tokens
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    //para generar un token
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))   //fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))   //fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Verifica si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Recupera todos los reclamos (claims) de un token JWT
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
