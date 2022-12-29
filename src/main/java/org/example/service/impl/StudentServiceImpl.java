package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public Student save(Student student) {
        return repository.save(student);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
