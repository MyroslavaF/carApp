package com.mfarion.carregistry.mappers;

import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.repositories.mappers.BrandEntityMapper;
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

class BrandEntityMapperTest {

    @InjectMocks
    private BrandEntityMapper mapper = Mappers.getMapper(BrandEntityMapper.class);

    @Mock
    private BrandEntityMapper brandEntityMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBrandEntityToBrand() {
        // Mockear el resultado del método brandEntityToBrandEntity
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        when(brandEntityMapper.brandEntityToBrand(any(BrandEntity.class))).thenReturn(brand);

        // Llamar al método bajo prueba
        Brand resultBrand = mapper.brandEntityToBrand(brandEntity);

        // Verificar el resultado
        assertEquals(brand.getId(), resultBrand.getId());
        assertEquals(brand.getName(), resultBrand.getName());
    }

    @Test
    void testBrandToBrandEntity() {
        // Mockear el resultado del método brandToBrandEntity
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        when(brandEntityMapper.brandToBrandEntity(any(Brand.class))).thenReturn(brandEntity);

        // Llamar al método bajo prueba
        BrandEntity resultBrandEntity = mapper.brandToBrandEntity(brand);

        // Verificar el resultado
        assertEquals(brandEntity.getId(), resultBrandEntity.getId());
        assertEquals(brandEntity.getName(), resultBrandEntity.getName());
    }
}
