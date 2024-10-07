package com.mfarion.carregistry.services;

import com.mfarion.carregistry.services.domain.model.Car;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface CarService {

    //obtiene una lista de todos los coches
    CompletableFuture<List<Car>> findAll();

    //busca un coche por su ID
    Car findById(Integer id);

    //crea un nuevo coche y lo asocia con una marca
    Car createCar(Car car, Integer brandId);

    //actualiza un coche existente
    Car update(Car car, Integer id);

    //elimina un coche por su ID
    void delete(Integer id);
}
