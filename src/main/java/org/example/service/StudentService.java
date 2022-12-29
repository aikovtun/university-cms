package org.example.service;

import org.example.entity.Student;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Optional<Student> findById(Long id);

    Optional<Student> findByUser(User user);

    List<Student> findAll();

    Page<Student> findAll(Pageable pageable);

    Student save(Student student);

    void deleteById(Long id);

}
