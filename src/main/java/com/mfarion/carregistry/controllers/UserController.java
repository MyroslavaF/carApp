package com.mfarion.carregistry.controllers;

import com.mfarion.carregistry.controllers.dtos.JwtResponse;
import com.mfarion.carregistry.controllers.dtos.LoginRequest;
import com.mfarion.carregistry.controllers.dtos.SignUpRequest;
import com.mfarion.carregistry.services.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody SignUpRequest request) {
        try {
            log.info("Sign up request: {}", request);
            return ResponseEntity.ok(authenticationService.signup(request));
        } catch (Exception e) {
            log.error("Sign up error: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("Login request: {}", request);
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
