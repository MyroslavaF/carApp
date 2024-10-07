package com.mfarion.carregistry.repositories;

import com.mfarion.carregistry.repositories.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad CarEntity.
 * Extiende JpaRepository para proporcionar métodos CRUD y operaciones adicionales de JPA.
 *
 */
@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {

}
