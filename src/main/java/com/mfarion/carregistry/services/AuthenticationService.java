package com.mfarion.carregistry.services;

import com.mfarion.carregistry.controllers.dtos.JwtResponse;
import com.mfarion.carregistry.controllers.dtos.LoginRequest;
import com.mfarion.carregistry.controllers.dtos.SignUpRequest;

public interface AuthenticationService {
    JwtResponse signup(SignUpRequest request);

    JwtResponse login(LoginRequest request);
}