package com.mfarion.carregistry.services.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una marca de coches en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente los métodos getter, setter,
 * equals, hashCode, toString, un constructor sin argumentos, y un constructor con todos los argumentos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    private Integer id;
    private String name;
    private Integer warranty;
    private String country;

}
