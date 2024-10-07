package com.mfarion.carregistry.controllers.mappers;

import com.mfarion.carregistry.controllers.dtos.BrandDTO;
import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.services.domain.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandEntity brandDTOtoBrandEntity(BrandDTO brandDTO);

    BrandDTO brandEntityToBrandDTO(BrandEntity brandEntity);

    Brand brandDTOtoBrand(BrandDTO brandDTO);

    Brand brandEntityToBrand(BrandEntity brandEntity);

}