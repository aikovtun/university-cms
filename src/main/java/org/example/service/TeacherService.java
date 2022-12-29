package org.example.service;

import org.example.entity.Teacher;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Optional<Teacher> findById(Long id);

    Optional<Teacher> findByUser(User user);

    List<Teacher> findAll();

    Page<Teacher> findAll(Pageable pageable);

    Teacher save(Teacher teacher);

    void deleteById(Long id);

}
