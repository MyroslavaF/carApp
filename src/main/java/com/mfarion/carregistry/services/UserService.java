package com.mfarion.carregistry.services;

import com.mfarion.carregistry.repositories.entities.UserEntity;

public interface UserService {
    UserEntity save(UserEntity newUser);
}