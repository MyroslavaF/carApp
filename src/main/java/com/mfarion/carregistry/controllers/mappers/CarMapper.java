package com.mfarion.carregistry.controllers.mappers;

import com.mfarion.carregistry.controllers.dtos.CarDTO;

import com.mfarion.carregistry.services.domain.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre objetos de dominio Car y objetos de transferencia de datos CarDTO.
 * Utiliza MapStruct para generar automáticamente las implementaciones de los métodos de mapeo.
 * La anotación @Mapper indica que esta es una interfaz de mapper y que el modelo de componentes
 * es manejado por Spring (componentModel = "spring").
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

    /**
     * Convierte un CarDTO en un objeto de dominio Car.
     *
     * @param carDTO el objeto de transferencia de datos que se va a convertir
     * @return el objeto de dominio Car
     */
    @Mapping(target = "brand", source = "brand")
    Car carDTOtoCar(CarDTO carDTO);

    /**
     * Convierte un objeto de dominio Car en un CarDTO, incluyendo la información de la marca.
     *
     * @param car el objeto de dominio que se va a convertir
     * @return el objeto de transferencia de datos CarDTO con la información de la marca
     */
    @Mapping(target = "brand", source = "brand")
    CarDTO carToCarDTOWithBrandId(Car car);

    /**
     * Convierte un objeto de dominio Car en un CarDTO.
     *
     * @param car el objeto de dominio que se va a convertir
     * @return el objeto de transferencia de datos CarDTO
     */
    CarDTO carToCarDTO(Car car);
}


