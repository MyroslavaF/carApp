package com.mfarion.carregistry.service;

import com.mfarion.carregistry.repositories.UserRepository;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import com.mfarion.carregistry.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
    }

    @Test
    void saveUser_Success() {
        // Configurar el mock del userRepository para que devuelva el usuario guardado
        when(userRepository.save(eq(userEntity))).thenReturn(userEntity);
        // Ejecutar el método bajo prueba
        UserEntity savedUser = userService.save(userEntity);

        // Asegurar que se devuelve un resultado no nulo y las aserciones necesarias
        assertEquals(userEntity.getId(), savedUser.getId());
        assertEquals(userEntity.getEmail(), savedUser.getEmail());
        assertEquals(userEntity.getPassword(), savedUser.getPassword());
    }
}