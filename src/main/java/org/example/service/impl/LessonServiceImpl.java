package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Lesson;
import org.example.repository.LessonRepository;
import org.example.service.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lesson> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lesson> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Lesson> findAllByCurrentUser(Pageable pageable) {
        return repository.findAllByCurrentUser(pageable);
    }

    @Override
    @Transactional
    public Lesson save(Lesson lesson) {
        return repository.save(lesson);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
