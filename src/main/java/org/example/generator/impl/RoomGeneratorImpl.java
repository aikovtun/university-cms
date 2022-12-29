package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Building;
import org.example.entity.Room;
import org.example.entity.RoomType;
import org.example.generator.DataGenerator;
import org.example.service.BuildingService;
import org.example.service.RoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class RoomGeneratorImpl implements DataGenerator {

    private final BuildingService buildingService;
    private final RoomService roomService;

    private int count;

    public void generate() {
        List<Building> buildings = buildingService.findAll();
        for (Building building : buildings) {
            for (int i = 1; i <= count; i++) {
                Room room = random();
                room.setBuilding(building);
                roomService.save(room);
            }
        }
    }

    public static Room random() {
        int number = ThreadLocalRandom.current().nextInt(100);
        Room room = new Room();
        room.setName("Room N" + number);
        room.setDescription("This is a room N" + number);
        room.setType(RoomType.values()[ThreadLocalRandom.current().nextInt(2)]);
        return room;
    }

    public int getCount() {
        return count;
    }

    public RoomGeneratorImpl setCount(int count) {
        this.count = count;
        return this;
    }

}
