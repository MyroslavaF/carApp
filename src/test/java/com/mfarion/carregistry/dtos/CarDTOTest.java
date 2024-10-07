package com.mfarion.carregistry.dtos;


import com.mfarion.carregistry.controllers.dtos.BrandDTO;
import com.mfarion.carregistry.controllers.dtos.CarDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarDTOTest {

    @Test
    void testCarDTO() {

        BrandDTO brandDTO = new BrandDTO(1, "Seat", 3, "Spain");
        CarDTO carDTO = new CarDTO(1, brandDTO, "Ibiza", 50000, 15000.0, 2020, "Compact sedan", "White", "Petrol", 4);


        assertEquals(1, carDTO.getId());
        assertEquals(brandDTO, carDTO.getBrand());
        assertEquals("Ibiza", carDTO.getModel());
        assertEquals(50000, carDTO.getMileage());
        assertEquals(15000.0, carDTO.getPrice());
        assertEquals(2020, carDTO.getYear());
        assertEquals("Compact sedan", carDTO.getDescription());
        assertEquals("White", carDTO.getColor());
        assertEquals("Petrol", carDTO.getFuelType());
        assertEquals(4, carDTO.getNumDoors());

        System.out.println(carDTO.toString());
    }
}