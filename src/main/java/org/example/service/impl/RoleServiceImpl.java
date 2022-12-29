package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.repository.RoleRepository;
import org.example.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return repository.save(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
