package com.mfarion.carregistry.services.impl;

import com.mfarion.carregistry.repositories.UserRepository;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import com.mfarion.carregistry.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not founded"));

    }

    @Override
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

}
