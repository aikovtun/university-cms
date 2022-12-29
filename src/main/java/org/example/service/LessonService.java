package org.example.service;

import org.example.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    Optional<Lesson> findById(Long id);

    List<Lesson> findAll();

    Page<Lesson> findAll(Pageable pageable);

    Page<Lesson> findAllByCurrentUser(Pageable pageable);

    Lesson save(Lesson lesson);

    void deleteById(Long id);

}
