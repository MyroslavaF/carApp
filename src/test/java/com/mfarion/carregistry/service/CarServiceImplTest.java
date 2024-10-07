package com.mfarion.carregistry.service;

import com.mfarion.carregistry.repositories.BrandRepository;
import com.mfarion.carregistry.repositories.CarRepository;
import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.repositories.mappers.BrandEntityMapper;
import com.mfarion.carregistry.repositories.mappers.CarEntityMapper;
import com.mfarion.carregistry.services.domain.model.Brand;
import com.mfarion.carregistry.services.domain.model.Car;
import com.mfarion.carregistry.services.impl.CarServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class) //indica que se usará Mockito para las pruebas unitarias
@SpringBootTest
class CarServiceImplTest {
    @Mock
    private BrandEntityMapper brandEntityMapper;
    @Mock
    private CarEntityMapper carEntityMapper;
    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private CarEntity carEntity;
    private Brand brand;
    private BrandEntity brandEntity;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(1);
        car.setModel("Ibiza");

        carEntity = new CarEntity();
        carEntity.setId(1);
        carEntity.setModel("Ibiza");

        brand = new Brand();
        brand.setId(1);
        brand.setName("Seat");

        brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setName("Seat");

    }

    @Test
    void createCar_Success() {

        when(carEntityMapper.carToCarEntity(eq(car))).thenReturn(carEntity);

        when(carEntityMapper.carEntityToCar(eq(carEntity))).thenReturn(car);

        when(brandRepository.findById(eq(brand.getId()))).thenReturn(Optional.of(brandEntity));

        when(carRepository.save(eq(carEntity))).thenReturn(carEntity);

        Car createdCar = carService.createCar(car, 1);

        assertNotNull(createdCar);
        assertEquals(car.getId(), createdCar.getId());
        assertEquals(car.getModel(), createdCar.getModel());
    }


    @Test
    void findById_Success() {
        when(carEntityMapper.carEntityToCar(eq(carEntity))).thenReturn(car);

        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));

        Car foundCar = carService.findById(1);

        assertNotNull(foundCar);
        assertEquals(car.getId(), foundCar.getId());
        assertEquals(car.getModel(), foundCar.getModel());
    }

    @Test
    void findAll_Success() throws ExecutionException, InterruptedException {
        // Configurar el comportamiento específico del mapper para car
        when(carEntityMapper.carEntityToCar(eq(carEntity))).thenReturn(car);

        // Configurar el mock del carRepository para que retorne una lista de carEntity
        List<CarEntity> carEntities = new ArrayList<>();
        carEntities.add(carEntity);
        when(carRepository.findAll()).thenReturn(carEntities);

        // Ejecutar el método bajo prueba
        CompletableFuture<List<Car>> futureCars = carService.findAll();
        List<Car> cars = futureCars.get();

        // Asegurar que se devuelve un resultado no nulo y las aserciones necesarias
        assertNotNull(cars);
        assertEquals(1, cars.size());
        assertEquals(car.getId(), cars.get(0).getId());
        assertEquals(car.getModel(), cars.get(0).getModel());
    }

    @Test
    void update_Success() {

        Car updatedCarData = new Car();
        updatedCarData.setId(1);
        updatedCarData.setModel("UpdatedModel");
        updatedCarData.setMileage(5000);
        updatedCarData.setPrice(15000.00);
        updatedCarData.setYear(2021);
        updatedCarData.setDescription("Updated description");
        updatedCarData.setColor("Red");
        updatedCarData.setFuelType("Petrol");
        updatedCarData.setNumDoors(4);
        updatedCarData.setBrand(brand);

        CarEntity updatedCarEntity = new CarEntity();
        updatedCarEntity.setId(1);
        updatedCarEntity.setModel("UpdatedModel");
        updatedCarEntity.setMileage(5000);
        updatedCarEntity.setPrice(15000.00);
        updatedCarEntity.setYear(2021);
        updatedCarEntity.setDescription("Updated description");
        updatedCarEntity.setColor("Red");
        updatedCarEntity.setFuelType("Petrol");
        updatedCarEntity.setNumDoors(4);
        updatedCarEntity.setBrand(brandEntity);

        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(carRepository.save(eq(carEntity))).thenReturn(updatedCarEntity);
        when(carEntityMapper.carEntityToCar(updatedCarEntity)).thenReturn(updatedCarData);

        Car updatedCar = carService.update(updatedCarData, 1);

        assertEquals(updatedCarData.getId(), updatedCar.getId());
        assertEquals(updatedCarData.getModel(), updatedCar.getModel());
        assertEquals(updatedCarData.getMileage(), updatedCar.getMileage());
        assertEquals(updatedCarData.getPrice(), updatedCar.getPrice());
        assertEquals(updatedCarData.getYear(), updatedCar.getYear());
        assertEquals(updatedCarData.getDescription(), updatedCar.getDescription());
        assertEquals(updatedCarData.getColor(), updatedCar.getColor());
        assertEquals(updatedCarData.getFuelType(), updatedCar.getFuelType());
        assertEquals(updatedCarData.getNumDoors(), updatedCar.getNumDoors());
        assertEquals(updatedCarData.getBrand().getId(), updatedCar.getBrand().getId());
        assertEquals(updatedCarData.getBrand().getName(), updatedCar.getBrand().getName());
    }


    @Test
    void delete_Success() {

        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));

        assertDoesNotThrow(() -> carService.delete(1));


    }

}


