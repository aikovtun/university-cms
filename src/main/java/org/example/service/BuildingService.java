package org.example.service;

import org.example.entity.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BuildingService {

    Optional<Building> findById(Long id);

    List<Building> findAll();

    Page<Building> findAll(Pageable pageable);

    Building save(Building building);

    void deleteById(Long id);

}
