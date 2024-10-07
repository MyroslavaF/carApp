package com.mfarion.carregistry.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un coche en la base de datos.
 * Utiliza Lombok para generar métodos getter, setter, constructores y otros métodos comunes.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class CarEntity {

    /**
     * Campo ID único para cada coche. Se genera automáticamente con una estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Relación muchos-a-uno con la entidad BrandEntity.
     * La carga es perezosa (LAZY) para optimizar el rendimiento.
     * Se une con la columna 'brand_id' de la tabla 'car'.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    private String model;
    private Integer mileage;
    private Double price;
    private Integer year;
    private String description;
    private String color;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "num_doors")
    private Integer numDoors;


}
