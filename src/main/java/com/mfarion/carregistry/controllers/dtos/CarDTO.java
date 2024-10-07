package com.mfarion.carregistry.controllers.dtos;

import lombok.*;

/**
 * Data Transfer Object (DTO) para la entidad Car.
 * Utiliza la anotación @Data de Lombok para generar automáticamente
 * los métodos getter, setter, equals, hashCode y toString.
 * Los DTO se utilizan para transferir datos entre las capas de la aplicación.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    private Integer id;
    private BrandDTO brand;

    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String color;
    private String fuelType;
    private Integer numDoors;


}
