package com.mfarion.carregistry.services.domain.model;

import lombok.*;

/**
 * Clase que representa un coche en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente los métodos getter, setter,
 * un constructor sin argumentos, y un constructor con todos los argumentos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private Integer id;
    private Brand brand;
    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String color;
    private String fuelType;
    private Integer numDoors;


}