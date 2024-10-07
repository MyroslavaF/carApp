package com.mfarion.carregistry.services.impl;

import com.mfarion.carregistry.repositories.BrandRepository;
import com.mfarion.carregistry.repositories.CarRepository;
import com.mfarion.carregistry.repositories.UserRepository;
import com.mfarion.carregistry.repositories.entities.BrandEntity;
import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import com.mfarion.carregistry.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;
    private final String[] HEADERS = {"id", "brand_id", "model", "price", " year", "description", "colour", "fuel_type", "num_doors"};


    //añade imagen del usuario
    @Override
    public void addUserImage(Long id, MultipartFile image) throws IOException {

        UserEntity entity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        log.info("Saving user image...");
        entity.setImage(image.getBytes());
        userRepository.save(entity);
    }

    //descarga imagen de usuario
    @Override
    public byte[] getUserImage(Long id) throws RuntimeException {

        UserEntity entity = userRepository.findById(id).orElseThrow(RuntimeException::new);
        if (entity == null || entity.getImage() == null)
            throw new RuntimeException("Image not found for id: " + id);

        return entity.getImage();
    }


    //sube los coches a base de datos
    @Override
    public List<CarEntity> uploadCars(MultipartFile file) {

        List<CarEntity> carList = new ArrayList<>();
        Map<Integer, BrandEntity> brandMap = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(bufferedReader,
                     CSVFormat.DEFAULT
                             .withFirstRecordAsHeader()
                             .withIgnoreHeaderCase()
                             .withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                Integer brandId = Integer.valueOf(csvRecord.get("brand_id"));
                String brandName = csvRecord.get("brand_name");
                Integer brandWarranty = Integer.valueOf(csvRecord.get("brand_warranty"));
                String brandCountry = csvRecord.get("brand_country");

                // si ya existe el brand
                BrandEntity brand = brandMap.get(brandId);
                if (brand == null) {
                    //si existe brand en base de datos
                    brand = brandRepository.findById(brandId).orElse(null);
                    if (brand == null) {
                        // si no esta se crea nuevo
                        brand = new BrandEntity();
                        brand.setId(brandId);
                        brand.setName(brandName);
                        brand.setWarranty(brandWarranty);
                        brand.setCountry(brandCountry);
                        brand = brandRepository.save(brand);
                    }
                    brandMap.put(brandId, brand);
                }

                CarEntity car = new CarEntity();
                car.setBrand(brand);
                car.setModel(csvRecord.get("model"));
                car.setMileage(Integer.valueOf(csvRecord.get("mileage")));
                car.setPrice(Double.valueOf(csvRecord.get("price")));
                car.setYear(Integer.valueOf(csvRecord.get("year")));
                car.setDescription(csvRecord.get("description"));
                car.setColor(csvRecord.get("color"));
                car.setFuelType(csvRecord.get("fuel_type"));
                car.setNumDoors(Integer.valueOf(csvRecord.get("num_doors")));
                carList.add(car);
                carRepository.save(car);
            }
        } catch (IOException e) {

            log.error("Failed to load users");
            throw new RuntimeException("Failed to load users");

        }

        return carList;

    }

    // get listas de coches desde base de datos en formato csv
    @Override
    public String carsCsv() {
        List<CarEntity> carList = carRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(Arrays.toString(HEADERS));

        for (CarEntity car : carList) {

            String brandName = car.getBrand().getName();
            Integer brandWarranty = car.getBrand().getWarranty();
            String brandCountry = car.getBrand().getCountry();

            csvContent.append(car.getId()).append(",")
                    .append(brandName).append(",")
                    .append(brandWarranty).append(",")
                    .append(brandCountry).append(",")
                    .append(car.getModel()).append(",")
                    .append(car.getMileage()).append(",")
                    .append(car.getPrice()).append(",")
                    .append(car.getYear()).append(",")
                    .append(car.getDescription()).append(",")
                    .append(car.getColor()).append(",")
                    .append(car.getFuelType()).append(",")
                    .append(car.getNumDoors()).append("\n");
        }
        return csvContent.toString();
    }

}