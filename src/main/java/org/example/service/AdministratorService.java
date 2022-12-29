package org.example.service;

import org.example.entity.Administrator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdministratorService {

    Optional<Administrator> findById(Long id);

    List<Administrator> findAll();

    Page<Administrator> findAll(Pageable pageable);

    Administrator save(Administrator administrator);

    void deleteById(Long id);

}
