package com.mfarion.carregistry.services.impl;

import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.repositories.BrandRepository;
import com.mfarion.carregistry.repositories.CarRepository;
import com.mfarion.carregistry.repositories.mappers.BrandEntityMapper;
import com.mfarion.carregistry.repositories.mappers.CarEntityMapper;
import com.mfarion.carregistry.services.CarService;
import com.mfarion.carregistry.services.domain.model.Brand;
import com.mfarion.carregistry.services.domain.model.Car;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    public static final String BRAND_NOT_FOUND_WITH_ID = "Brand not found with id ";
    public static final String CAR_NOT_FOUND_WITH_ID = "Car not found with id ";

    private CarRepository carRepository;

    private BrandRepository brandRepository;

    private CarEntityMapper carEntityMapper;

    private final BrandEntityMapper brandEntityMapper;

    /**
     * Obtiene todos los coches de la base de datos de manera asíncrona.
     * Convierte las entidades de coches a objetos de dominio Car.
     * Devuelve un CompletableFuture con la lista de coches.
     * Transactional indica que el método debe ejecutarse dentro de una transacción.
     * en este caso, 'readOnly = true' especifica que la transacción es de solo lectura,
     */
    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<Car>> findAll() {
        long startTime = System.currentTimeMillis();
        // Obtiene todas las entidades del repositorio
        List<CarEntity> carEntities = carRepository.findAll();
        // Convertir entidades a objetos Car
        List<Car> cars = carEntities.stream()
                .map(carEntityMapper::carEntityToCar)
                .toList();
        log.info("List: " + Thread.currentThread().getName());
        // Calcula el tiempo de la operación
        long endTime = System.currentTimeMillis();
        log.info("Total time: " + (endTime - startTime));
        // Devuelve CompletableFuture ya completado con la lista de coches
        return CompletableFuture.completedFuture(cars);

    }

    /**
     * Encuentra un coche por su ID de manera asíncrona.
     * Convierte la entidad del coche encontrada a un objeto de dominio Car.
     * Devuelve un CompletableFuture con el coche encontrado.
     * Lanza EntityNotFoundException si el coche no existe.
     */
    @Override
    public Car findById(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CAR_NOT_FOUND_WITH_ID + id));

        return carEntityMapper.carEntityToCar(carEntity);
    }

    /**
     * Crea un nuevo coche de manera asíncrona.
     * Asocia el coche con la marca especificada por brandId.
     * Guarda el coche en la base de datos y devuelve el coche creado.
     * Lanza EntityNotFoundException si la marca no existe con el ID especificado.
     */
    public Car createCar(Car car, Integer brandId) {

        if (brandId == null) {
            throw new IllegalArgumentException("Brand ID must not be null");
        }

        //revisa si id de brand existe
        BrandEntity brandEntity = brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + brandId));

        car.setBrand(brandEntityMapper.brandEntityToBrand(brandEntity));
        CarEntity carEntity = carEntityMapper.carToCarEntity(car);
        //convierte Car a CarEntity para guardar en bbdd y decolver el coche creado y convertido a Car
        CarEntity savedCarEntity = carRepository.save(carEntity);
        return carEntityMapper.carEntityToCar(savedCarEntity);
    }

    /**
     * Actualiza un coche existente por su ID de manera asíncrona.
     * Asocia el coche actualizado con la marca especificada por brandId.
     * Guarda el coche actualizado en la base de datos y devuelve el coche actualizado.
     * Lanza EntityNotFoundException si el coche no existe con el ID especificado.
     */
    @Override
    public Car update(Car car, Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CAR_NOT_FOUND_WITH_ID " + id));
        carEntity.setModel(car.getModel());
        carEntity.setMileage(car.getMileage());
        carEntity.setPrice(car.getPrice());
        carEntity.setYear(car.getYear());
        carEntity.setDescription(car.getDescription());
        carEntity.setColor(car.getColor());
        carEntity.setFuelType(car.getFuelType());
        carEntity.setNumDoors(car.getNumDoors());
        carEntity.setBrand(brandRepository.findById(car.getBrand().getId()).orElseThrow(() -> new EntityNotFoundException(BRAND_NOT_FOUND_WITH_ID + car.getBrand().getId())));

        if (car.getBrand() != null && car.getBrand().getId() != null) {
            BrandEntity brandEntity = brandRepository.findById(car.getBrand().getId())
                    .orElseThrow(() -> new EntityNotFoundException(BRAND_NOT_FOUND_WITH_ID + car.getBrand().getId()));

            Brand brand = brandEntityMapper.brandEntityToBrand(brandEntity);
            // si necesito convertir de nuevo a Entity:
            carEntity.setBrand(brandEntityMapper.brandToBrandEntity(brand));
        } else {
            throw new IllegalArgumentException("Brand ID must be specified for updating a Car");
        }
        CarEntity updatedCarEntity = carRepository.save(carEntity);
        return carEntityMapper.carEntityToCar(updatedCarEntity);

    }

    @Override
    public void delete(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CAR_NOT_FOUND_WITH_ID + id));
        carRepository.delete(carEntity);
    }
}

