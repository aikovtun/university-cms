package org.example.service;

import org.example.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    Optional<Subject> findById(Long id);

    List<Subject> findAll();

    Page<Subject> findAll(Pageable pageable);

    Subject save(Subject subject);

    void deleteById(Long id);

}
