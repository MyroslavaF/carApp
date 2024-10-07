package com.mfarion.carregistry.mappers;

import com.mfarion.carregistry.controllers.dtos.BrandDTO;
import com.mfarion.carregistry.controllers.dtos.CarDTO;
import com.mfarion.carregistry.controllers.mappers.CarMapper;
import com.mfarion.carregistry.services.domain.model.Brand;
import com.mfarion.carregistry.services.domain.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarMapperTest {

    @Mock
    private BrandDTO mockBrand;

    private CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    void testCarDTOtoCarMapping() {
        // Crear un objeto CarDTO simulado
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1);
        carDTO.setBrand(mockBrand); // Aquí podrías configurar las propiedades del mockBrand si fuera necesario
        carDTO.setModel("Camry");
        carDTO.setMileage(50000);
        carDTO.setPrice(25000.0);
        carDTO.setYear(2020);
        carDTO.setDescription("A great car");
        carDTO.setColor("Red");
        carDTO.setFuelType("Gasoline");
        carDTO.setNumDoors(4);

        // Configurar el comportamiento esperado del mockBrand
        when(mockBrand.getId()).thenReturn(1);
        when(mockBrand.getName()).thenReturn("Toyota");
        when(mockBrand.getWarranty()).thenReturn(5);
        when(mockBrand.getCountry()).thenReturn("Japan");

        // Llamar al método de mapeo
        Car car = carMapper.carDTOtoCar(carDTO);

        // Verificar que el mapeo no es nulo
        assertNotNull(car);

        // Verificar que todos los campos se han mapeado correctamente
        assertEquals(carDTO.getId(), car.getId());
        assertEquals(carDTO.getModel(), car.getModel());
        assertEquals(carDTO.getMileage(), car.getMileage());
        assertEquals(carDTO.getPrice(), car.getPrice());
        assertEquals(carDTO.getYear(), car.getYear());
        assertEquals(carDTO.getDescription(), car.getDescription());
        assertEquals(carDTO.getColor(), car.getColor());
        assertEquals(carDTO.getFuelType(), car.getFuelType());
        assertEquals(carDTO.getNumDoors(), car.getNumDoors());

        // Verificar el mapeo del Brand (si es necesario)
        assertEquals(carDTO.getBrand().getId(), car.getBrand().getId());
        assertEquals(carDTO.getBrand().getName(), car.getBrand().getName());
        assertEquals(carDTO.getBrand().getWarranty(), car.getBrand().getWarranty());
        assertEquals(carDTO.getBrand().getCountry(), car.getBrand().getCountry());
    }
}
