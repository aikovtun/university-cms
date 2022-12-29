package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Teacher;
import org.example.entity.User;
import org.example.repository.TeacherRepository;
import org.example.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public Teacher save(Teacher teacher) {
        return repository.save(teacher);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
