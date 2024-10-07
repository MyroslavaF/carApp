package com.mfarion.carregistry.repositories.mappers;

import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.services.domain.model.Brand;
import org.mapstruct.Mapper;

/**
 * Mapper para convertir entre entidades de la base de datos y objetos de dominio para la clase Brand.
 * Utiliza MapStruct para generar automáticamente las implementaciones de los métodos de mapeo.
 * La anotación @Mapper indica que esta es una interfaz de mapper y que el modelo de componentes
 * es manejado por Spring (componentModel = "spring").
 */
@Mapper(componentModel = "spring")
public interface BrandEntityMapper {

    Brand brandEntityToBrand(BrandEntity brandEntity);
    BrandEntity brandToBrandEntity(Brand brand);
}
