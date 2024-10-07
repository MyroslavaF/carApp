package com.mfarion.carregistry.controllers.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    String token;

}
