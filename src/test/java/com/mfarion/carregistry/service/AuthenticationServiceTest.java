package com.mfarion.carregistry.service;

import com.mfarion.carregistry.config.PasswordConfig;
import com.mfarion.carregistry.controllers.dtos.JwtResponse;
import com.mfarion.carregistry.controllers.dtos.LoginRequest;
import com.mfarion.carregistry.controllers.dtos.SignUpRequest;
import com.mfarion.carregistry.repositories.UserRepository;
import com.mfarion.carregistry.repositories.entities.Role;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import com.mfarion.carregistry.services.impl.AuthenticationServiceImpl;
import com.mfarion.carregistry.services.impl.JwtService;
import com.mfarion.carregistry.services.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordConfig passwordConfig;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void signupTest() {
        // Configurar datos de prueba
        SignUpRequest signUpRequest = new SignUpRequest("Jose", "Fernandez", "mail@example.com", "password");

        // Mock para el método encode del passwordEncoder
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn(encodedPassword);

        // Crear la entidad de usuario esperada que será devuelta por el UserService
        UserEntity userEntity = UserEntity.builder()
                .name(signUpRequest.getName())
                .surname(signUpRequest.getSurname())
                .email(signUpRequest.getEmail())
                .password(encodedPassword) // Usar la contraseña codificada
                .role(Role.ROLE_CLIENT)
                .build();

        // Configurar el mock del UserService para devolver la entidad de usuario
        when(userService.save(any(UserEntity.class))).thenReturn(userEntity);

        // Mock para el método generateToken del JwtService
        String mockJwtToken = "mockJwtToken";
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(mockJwtToken);

        // Ejecutar el método bajo prueba
        JwtResponse jwtResponse = authenticationService.signup(signUpRequest);

        // Verificar el resultado
        assertEquals(mockJwtToken, jwtResponse.getToken());
        // Verificar que se llamó al método save del UserService con los parámetros correctos
        verify(userService).save(any(UserEntity.class));
        // Verificar que se llamó al método generateToken del JwtService con los parámetros correctos
        verify(jwtService).generateToken(userEntity);
    }
    @Test
    void loginTest() {
        // Configurar datos de prueba
        LoginRequest loginRequest = new LoginRequest("mail@example.com","password");

        // Mock para el método authenticate del AuthenticationManager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Mock para el método findByEmail del UserRepository
        UserEntity userEntity = UserEntity.builder()
                .name("Jose")
                .surname("Fernandez")
                .email("mail@example.com")
                .password("encodedPassword")
                .role(Role.ROLE_CLIENT)
                .build();
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(java.util.Optional.of(userEntity));

        // Mock para el método generateToken del JwtService
        String mockJwtToken = "mockJwtToken";
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(mockJwtToken);

        // Ejecutar el método bajo prueba
        JwtResponse jwtResponse = authenticationService.login(loginRequest);

        // Verificar el resultado
        assertEquals(mockJwtToken, jwtResponse.getToken());
    }
}