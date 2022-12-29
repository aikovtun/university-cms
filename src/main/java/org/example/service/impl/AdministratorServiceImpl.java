package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Administrator;
import org.example.repository.AdministratorRepository;
import org.example.service.AdministratorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Administrator> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Administrator> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Administrator> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public Administrator save(Administrator administrator) {
        return repository.save(administrator);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
