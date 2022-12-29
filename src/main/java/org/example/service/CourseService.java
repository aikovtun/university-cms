package org.example.service;

import org.example.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<Course> findById(Long id);

    List<Course> findAll();

    Page<Course> findAll(Pageable pageable);

    Course save(Course course);

    void deleteById(Long id);

}
