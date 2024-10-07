package com.mfarion.carregistry.services.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Clase que representa el Usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;
    private String image;

}