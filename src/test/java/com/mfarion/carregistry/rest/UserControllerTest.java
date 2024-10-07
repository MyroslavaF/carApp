package com.mfarion.carregistry.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfarion.carregistry.controllers.CarController;
import com.mfarion.carregistry.controllers.UserController;
import com.mfarion.carregistry.controllers.dtos.JwtResponse;
import com.mfarion.carregistry.controllers.dtos.LoginRequest;
import com.mfarion.carregistry.controllers.dtos.SignUpRequest;

import com.mfarion.carregistry.filter.JwtAuthenticationFilter;

import com.mfarion.carregistry.services.impl.AuthenticationServiceImpl;
import com.mfarion.carregistry.services.impl.JwtService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @MockBean
    private CarController carController;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void signup_Success() throws Exception {
        // solicitud de prueba
        SignUpRequest request = new SignUpRequest("name","surname", "email", "password");

        // simula servicio de autenticación para devolver un JWTResponse exit
        JwtResponse jwtResponse = new JwtResponse("token");
        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(jwtResponse);

        //verificar el resultado
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithMockUser
    void signup_Failure() throws Exception {
        SignUpRequest request = new SignUpRequest("name","surname", "email", "password");

        doThrow(EntityNotFoundException.class).when(authenticationService).signup(request);

        //verifica si devuelve 400 Bad Request
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


    }



    @Test
    @WithMockUser
    void login_Success() throws Exception {
        //solicitud de prueba
        LoginRequest request = new LoginRequest("username", "password");

        //simula el servicio de autenticación para devolver un JWTResponse exitoso
        JwtResponse jwtResponse = new JwtResponse("token");
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        //el resultado
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithMockUser
    void login_Failure() throws Exception {
        LoginRequest request = new LoginRequest("testUser", "password");

        doThrow(new RuntimeException("Login error")).when(authenticationService).login(request);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());



    }


}
