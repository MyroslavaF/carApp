package com.mfarion.carregistry.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa una marca en la base de datos.
 * Utiliza Lombok para generar métodos getter, setter, constructores y otros métodos comunes.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brand")
public class BrandEntity {

    /**
     * Campo ID único para cada marca. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer warranty;
    private String country;

    /**
     * Relación uno-a-muchos con la entidad CarEntity.
     * La lista carEntityList contiene todos los coches asociados a esta marca.
     * El mapeo se realiza a través del campo "brand" en la entidad CarEntity.
     */
    @OneToMany(mappedBy = "brand" /*cascade = CascadeType.ALL*/)
    private List<CarEntity> carEntityList;

}