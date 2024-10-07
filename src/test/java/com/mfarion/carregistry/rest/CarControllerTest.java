package com.mfarion.carregistry.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfarion.carregistry.controllers.CarController;

import com.mfarion.carregistry.controllers.UserController;
import com.mfarion.carregistry.controllers.dtos.BrandDTO;
import com.mfarion.carregistry.controllers.dtos.CarDTO;
import com.mfarion.carregistry.controllers.mappers.CarMapper;
import com.mfarion.carregistry.filter.JwtAuthenticationFilter;
import com.mfarion.carregistry.services.AuthenticationService;
import com.mfarion.carregistry.services.CarService;
import com.mfarion.carregistry.services.domain.model.Brand;
import com.mfarion.carregistry.services.domain.model.Car;
import com.mfarion.carregistry.services.impl.JwtService;
import com.mfarion.carregistry.services.impl.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @MockBean
    private CarService carService;

    @MockBean
    private CarMapper carMapper;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "CLIENT")
    void getAllCars_Success() throws Exception {

        configureMockBehaviorForGetAllCars();

        mockMvc.perform(get("/cars/getCars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCarDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].brand.name").value("Seat"))
                .andExpect(jsonPath("$[0].model").value("Ibiza"))
                .andExpect(jsonPath("$[0].mileage").value(10000));
    }


    @Test
    @WithMockUser(username = "test", password = "test", roles = "CLIENT")
    void getCarById_Success() throws Exception {

        configureMockBehaviorForGetByIdCars();
                mockMvc.perform(get("/cars/cars/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void createCar_Success() throws Exception {

        configureMockBehaviorForCreateCar();

        mockMvc.perform(post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCarDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void createCar_BadRequestNoBrand() throws Exception {

        CarDTO carDTO= new CarDTO(1, null, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        configureMockBehaviorForCreateCar();

        // Hacer una solicitud POST y verificar que devuelve 400 Bad Request
        mockMvc.perform(post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void createCar_BadRequestOnException() throws Exception {

        Brand brand = new Brand(1, "Seat", 5, "Spain");
        BrandDTO brandDTO = new BrandDTO(1, "Seat", 5, "Spain");
        CarDTO carDTO= new CarDTO(1, brandDTO, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        Car car = new Car(1, brand, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        when(carMapper.carDTOtoCar(eq(carDTO))).thenReturn(car);

        when(carService.createCar(car, brand.getId())).thenReturn(car);



        // Simular que carService.createCar lanza una excepción
        doThrow(new EntityNotFoundException()).when(carService).createCar(eq(car), eq(brandDTO.getId()));


        // Hacer una solicitud POST y verificar que devuelve 400 Bad Request
        mockMvc.perform(post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void updateCar_Success() throws Exception {

        CarDTO carDTO = createCarDTO();
        Car updatedCar = new Car(1, createBrand(), "Novey", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);

        when(carMapper.carDTOtoCar(eq(carDTO))).thenReturn(updatedCar);

        when(carService.update(eq(updatedCar), eq(1))).thenReturn(updatedCar);

        mockMvc.perform(put("/cars/updateCar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCarDTO())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void updateCar_NotFoundExc() throws Exception{
        CarDTO carDTO = new CarDTO(1, null, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        Car car = new Car(1, new Brand(1, "Seat", 2, "Spain"), "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);

        when(carMapper.carDTOtoCar(carDTO)).thenReturn(car);

        doThrow(new EntityNotFoundException()).when(carService).update(eq(car), eq(3));

        mockMvc.perform(put("/cars/updateCar/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isNotFound());
    }



    @Test
    @WithMockUser(username = "test", password = "test", roles = "VENDOR")
    void deleteCar_Success() throws Exception {

        doNothing().when(carService).delete(1);

        mockMvc.perform(delete("/cars/deleteCar/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser (username= "test", password = "test", roles = "VENDOR")
    void deleteCar_NotFoundedExc() throws Exception {
        doThrow(new EntityNotFoundException()).when(carService).delete(1);

        mockMvc.perform(delete("/cars/deleteCar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    private void configureMockBehaviorForGetAllCars() {
        // comportamiento del servicio y del mapper para getAllCars
        Brand brand = new Brand(1, "Seat", 5, "Spain");
        Car car1 = new Car(1, brand, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        List<Car> carList = List.of(car1);
        when(carService.findAll()).thenReturn(CompletableFuture.completedFuture(carList));
        when(carMapper.carToCarDTO(car1)).thenReturn(createCarDTO());
    }

    private void configureMockBehaviorForGetByIdCars() {
        // comportamiento del servicio y del mapper para getAllCars
        Brand brand = new Brand(1, "Seat", 5, "Spain");
        Car car1 = new Car(1, brand, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        when(carService.findById(1)).thenReturn(car1);
        when(carMapper.carToCarDTO(car1)).thenReturn(createCarDTO());
    }

    private void configureMockBehaviorForCreateCar() {
        // Configuración del comportamiento del servicio y del mapper para createCar
        Brand brand = new Brand(1, "Seat", 5, "Spain");
        BrandDTO brandDTO = new BrandDTO(1, "Seat", 5, "Spain");
        CarDTO carDTO= new CarDTO(1, brandDTO, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        Car car = new Car(1, brand, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
        when(carMapper.carDTOtoCar(carDTO)).thenReturn(car);

        when(carService.createCar(car, brand.getId())).thenReturn(car);

        when(carMapper.carToCarDTOWithBrandId(car)).thenReturn(carDTO);
    }

    private CarDTO createCarDTO() {
        // Crear y devolver un objeto CarDTO para las pruebas
        BrandDTO brandDTO = new BrandDTO(1, "Seat", 5, "Spain");
        return new CarDTO(1, brandDTO, "Ibiza", 10000, 20000.0, 2019, "Description", "Red", "Gasoline", 4);
    }

    private Brand createBrand() {
        // Crear y devolver un objeto Brand para las pruebas
        return new Brand(1, "Seat", 5, "Spain");
    }
}




