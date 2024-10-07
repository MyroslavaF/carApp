package com.mfarion.carregistry.services;

import com.mfarion.carregistry.repositories.entities.CarEntity;
import com.mfarion.carregistry.repositories.entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void addUserImage(Long id, MultipartFile imageFile) throws IOException;

    byte[] getUserImage(Long id) throws RuntimeException;

    List<CarEntity> uploadCars(MultipartFile file);

    String carsCsv();
}
