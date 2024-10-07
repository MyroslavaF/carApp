package com.mfarion.carregistry.services.impl;

import com.mfarion.carregistry.controllers.dtos.JwtResponse;
import com.mfarion.carregistry.controllers.dtos.LoginRequest;
import com.mfarion.carregistry.controllers.dtos.SignUpRequest;
import com.mfarion.carregistry.repositories.UserRepository;
import com.mfarion.carregistry.repositories.entities.Role;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import com.mfarion.carregistry.services.AuthenticationService;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse signup(SignUpRequest request) {
        var user = UserEntity
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CLIENT)
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtResponse.builder().token(jwt).build();
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid user or password"));

        var jwt = jwtService.generateToken(user);
        return JwtResponse.builder().token(jwt).build();
    }
}
