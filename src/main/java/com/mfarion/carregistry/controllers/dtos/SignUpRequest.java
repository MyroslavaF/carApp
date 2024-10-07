package com.mfarion.carregistry.controllers.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    String name;
    String surname;
    String email;
    String password;

}
