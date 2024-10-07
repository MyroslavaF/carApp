package com.mfarion.carregistry.repositories;

import com.mfarion.carregistry.repositories.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repositorio para la entidad BrandEntity.
 * Extiende JpaRepository para proporcionar métodos CRUD y operaciones adicionales de JPA.
 */
@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
}
