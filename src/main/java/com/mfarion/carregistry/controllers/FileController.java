package com.mfarion.carregistry.controllers;

import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.services.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    //endpoint para poder añadir coches a la base de datos.
    @PostMapping(value = "/uploadCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> uploadCsv(@RequestParam(value = "file") MultipartFile file) {

        if (file.isEmpty()) {
            log.error("The file it`s empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            List<CarEntity> cars = fileService.uploadCars(file);
            return ResponseEntity.ok("File successfully uploaded and " + cars.size() + " cars saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file: " + e.getMessage());
        }
    }


    //descargar los datos de los coches almacenados en la base de datos en formato CSV
    @GetMapping(value = "/downloadCars")
    @PreAuthorize("hasAnyRole('VENDOR', 'CLIENT')")
    public ResponseEntity<?> downloadFIle() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "cars.csv");

        byte[] csvBytes = fileService.carsCsv().getBytes();
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }


    //endpoint que almacene una imagen para un usuario que ya existe
    @PostMapping(value = "/userImage/{id}/add")
    @PreAuthorize("hasAnyRole('VENDOR', 'CLIENT')")
    public ResponseEntity<String> addProduct(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                log.error("The file is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is empty");
            }
            if (!Objects.requireNonNull(file.getOriginalFilename()).contains(".png") && !file.getOriginalFilename().contains(".jpg")) {
                log.error("The file is not a valid image format");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is not a valid image format");
            }
            fileService.addUserImage(id, file);
            return ResponseEntity.ok("Image successfuly saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // endpoint adicional para poder descargar la imagen
    @GetMapping("/userImage/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'CLIENT')")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        try {
            byte[] imageBytes = fileService.getUserImage(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
