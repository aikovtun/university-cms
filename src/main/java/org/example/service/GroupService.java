package org.example.service;

import org.example.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Optional<Group> findById(Long id);

    List<Group> findAll();

    Page<Group> findAll(Pageable pageable);

    Group save(Group group);

    void deleteById(Long id);

}
