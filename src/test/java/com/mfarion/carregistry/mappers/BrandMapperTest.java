package com.mfarion.carregistry.mappers;

import com.mfarion.carregistry.controllers.dtos.BrandDTO;
import com.mfarion.carregistry.controllers.mappers.BrandMapper;
import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.services.domain.model.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BrandMapperTest {

    @InjectMocks
    private BrandMapper mapper = Mappers.getMapper(BrandMapper.class);

    @Mock
    private BrandMapper brandMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBrandDTOtoBrandEntity() {
        // Mockear el resultado del método brandDTOtoBrandEntity
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(1);
        brandDTO.setName("Toyota");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        when(brandMapper.brandDTOtoBrandEntity(any(BrandDTO.class))).thenReturn(brandEntity);

        // Llamar al método bajo prueba
        BrandEntity resultBrandEntity = mapper.brandDTOtoBrandEntity(brandDTO);

        // Verificar el resultado
        assertEquals(brandEntity.getId(), resultBrandEntity.getId());
        assertEquals(brandEntity.getName(), resultBrandEntity.getName());
    }

    @Test
    void testBrandEntityToBrandDTO() {
        // Mockear el resultado del método brandEntityToBrandDTO
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(1);
        brandDTO.setName("Toyota");

        when(brandMapper.brandEntityToBrandDTO(any(BrandEntity.class))).thenReturn(brandDTO);

        // Llamar al método bajo prueba
        BrandDTO resultBrandDTO = mapper.brandEntityToBrandDTO(brandEntity);

        // Verificar el resultado
        assertEquals(brandDTO.getId(), resultBrandDTO.getId());
        assertEquals(brandDTO.getName(), resultBrandDTO.getName());
    }

    @Test
    void testBrandDTOtoBrand() {
        // Mockear el resultado del método brandDTOtoBrand
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(1);
        brandDTO.setName("Toyota");

        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        when(brandMapper.brandDTOtoBrand(any(BrandDTO.class))).thenReturn(brand);

        // Llamar al método bajo prueba
        Brand resultBrand = mapper.brandDTOtoBrand(brandDTO);

        // Verificar el resultado
        assertEquals(brand.getId(), resultBrand.getId());
        assertEquals(brand.getName(), resultBrand.getName());
    }

    @Test
    void testBrandEntityToBrand() {
        // Mockear el resultado del método brandEntityToBrand
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        when(brandMapper.brandEntityToBrand(any(BrandEntity.class))).thenReturn(brand);

        // Llamar al método bajo prueba
        Brand resultBrand = mapper.brandEntityToBrand(brandEntity);

        // Verificar el resultado
        assertEquals(brand.getId(), resultBrand.getId());
        assertEquals(brand.getName(), resultBrand.getName());
    }
}
