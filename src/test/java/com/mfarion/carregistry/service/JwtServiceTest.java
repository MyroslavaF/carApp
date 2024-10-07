package com.mfarion.carregistry.service;

import com.mfarion.carregistry.services.impl.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Use a properly Base64-encoded secret key
        jwtService.jwtSecretKey = "dHdvcGFwZXJpdHNlbGZ6b29kZWVwZ2FtZXBsYXN0aWNjb21wb3NlZG1lbWJlcmJveG0"; // Replace with your actual Base64-encoded key
        jwtService.jwtExpirationMs = 3600000L; // 1 hour
    }

    @Test
    void testExtractUserName() {
        String token = generateTestToken("testUser");
        String username = jwtService.extractUserName(token);
        assertEquals("testUser", username);
    }

    @Test
    void testIsTokenValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = generateTestToken("testUser");
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenInvalidWithWrongUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("wrongUser");
        String token = generateTestToken("testUser");
        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void testIsTokenNotExpired() {
        String token = generateTestToken("testUser");
        assertFalse(jwtService.isTokenExpired(token));
    }

    private String generateTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(jwtService.getSigningKey())
                .compact();
    }

    private String generateExpiredToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // 2 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // 1 hour ago
                .signWith(jwtService.getSigningKey())
                .compact();
    }
}
