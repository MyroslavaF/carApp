package com.mfarion.carregistry.repositories.mappers;

import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.services.domain.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BrandEntityMapper.class})
public interface CarEntityMapper {

    Car carEntityToCar(CarEntity carEntity);

    @Mapping(target = "brand", source = "brand")
    CarEntity carToCarEntity(Car car);
}
