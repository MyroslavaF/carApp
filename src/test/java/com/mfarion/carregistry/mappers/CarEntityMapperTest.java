package com.mfarion.carregistry.mappers;

import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.repositories.mappers.BrandEntityMapper;
import com.mfarion.carregistry.repositories.mappers.CarEntityMapper;
import com.mfarion.carregistry.services.domain.model.Brand;
import com.mfarion.carregistry.services.domain.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CarEntityMapperTest {

    @InjectMocks
    private CarEntityMapper carEntityMapper = Mappers.getMapper(CarEntityMapper.class);

    @Mock
    private BrandEntityMapper brandEntityMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCarEntityToCar() {
        // Mockear BrandEntityMapper
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        when(brandEntityMapper.brandEntityToBrand(any(BrandEntity.class))).thenReturn(brand);

        // Crear un CarEntity de prueba
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1);
        carEntity.setModel("Corolla");
        carEntity.setYear(2021);
        carEntity.setBrand(brandEntity);

        // Llamar al método de mapeo
        Car car = carEntityMapper.carEntityToCar(carEntity);

        // Verificar el resultado
        assertNotNull(car);
        assertEquals(carEntity.getId(), car.getId());
        assertEquals(carEntity.getModel(), car.getModel());
        assertEquals(carEntity.getYear(), car.getYear());
        assertEquals(carEntity.getBrand().getId(), car.getBrand().getId());
        assertEquals(carEntity.getBrand().getName(), car.getBrand().getName());
    }

    @Test
    void testCarToCarEntity() {
        // Mockear BrandEntityMapper
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Toyota");

        when(brandEntityMapper.brandToBrandEntity(any(Brand.class))).thenReturn(brandEntity);

        // Crear un Car de prueba
        Car car = new Car();
        car.setId(1);
        car.setModel("Corolla");
        car.setYear(2021);
        car.setBrand(brand);

        // Llamar al método de mapeo
        CarEntity carEntity = carEntityMapper.carToCarEntity(car);

        // Verificar el resultado
        assertNotNull(carEntity);
        assertEquals(car.getId(), carEntity.getId());
        assertEquals(car.getModel(), carEntity.getModel());
        assertEquals(car.getYear(), carEntity.getYear());
        assertEquals(car.getBrand().getId(), carEntity.getBrand().getId());
        assertEquals(car.getBrand().getName(), carEntity.getBrand().getName());
    }
}