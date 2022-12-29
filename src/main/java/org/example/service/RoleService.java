package org.example.service;

import org.example.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    Page<Role> findAll(Pageable pageable);

    Role save(Role role);

    void deleteById(Long id);

}
