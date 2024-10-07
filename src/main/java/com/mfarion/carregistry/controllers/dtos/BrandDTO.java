package com.mfarion.carregistry.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para la entidad Brand.
 * Utiliza la anotación @Data de Lombok para generar automáticamente
 * los métodos getter, setter, equals, hashCode y toString.
 * Los DTO se utilizan para transferir datos entre las capas de la aplicación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {

    private Integer id;
    private String name;
    private int warranty;
    private String country;

}
