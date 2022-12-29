package org.example.service;

import org.example.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Optional<Room> findById(Long id);

    List<Room> findAll();

    Page<Room> findAll(Pageable pageable);

    Room save(Room room);

    void deleteById(Long id);

}
