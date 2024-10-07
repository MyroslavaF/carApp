package com.mfarion.carregistry.controllers;

import com.mfarion.carregistry.controllers.dtos.CarDTO;
import com.mfarion.carregistry.controllers.mappers.CarMapper;

import com.mfarion.carregistry.services.CarService;
import com.mfarion.carregistry.services.domain.model.Car;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    private final CarMapper carMapper;

    //Endpoint para obtener todos los coches.
    @GetMapping("/getCars")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CarDTO>> getAllCars() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authorities: {}", authentication.getAuthorities());
        List<Car> cars = carService.findAll().get();

        // Transforma la lista de coches a una lista de CarDTO
        List<CarDTO> carDTOs = cars.stream()
                .map(carMapper::carToCarDTO)
                .toList();

        return ResponseEntity.ok().body(carDTOs);
    }

    //Endpoint para obtener coche por su ID
    @GetMapping("/cars/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authorities: {}", authentication.getAuthorities());

        try {
            Car car = carService.findById(id);
            CarDTO carDTO = carMapper.carToCarDTO(car);

            return ResponseEntity.ok().body(carDTO);
        } catch (Exception ex) {
            log.error("Error occurred while fetching car", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //Endpoint para crear un coche nuevo
    @PostMapping("/addCar")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        // Revisa si brand id existe
        if (carDTO.getBrand() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Convierte CarDTO a Car
        Car car = carMapper.carDTOtoCar(carDTO);
        // Devuelve el coche creado
        try {
            Car savedCar = carService.createCar(car, carDTO.getBrand().getId());
            return ResponseEntity
                    .ok()
                    .body(carMapper.carToCarDTOWithBrandId(savedCar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //Endpoint para actualizar los datos del coche por su ID
    @PutMapping("/updateCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<CarDTO> updateCar(@RequestBody CarDTO carDTO, @PathVariable Integer id) {
        try {
            Car car = carMapper.carDTOtoCar(carDTO);
            Car updatedCar = carService.update(car, id);
            return ResponseEntity.ok().body(carMapper.carToCarDTOWithBrandId(updatedCar));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint para eliminar el coche por su ID
    @DeleteMapping("/deleteCar/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> deleteCar(@PathVariable Integer id) {

        try {
            carService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}