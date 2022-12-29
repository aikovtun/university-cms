package org.example.service;

import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User save(User user);

    void deleteById(Long id);

}
