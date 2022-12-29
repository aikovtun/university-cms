package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Building;
import org.example.generator.DataGenerator;
import org.example.service.BuildingService;
import org.example.utils.Randomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuildingGeneratorImpl implements DataGenerator {

    private final BuildingService buildingService;

    private static final String[] ALL_NAMES = {
        "Environmental Design Building", "Engineering Center",
        "Center for Astrophysics and Space Astronomy", "Computing Center",
        "Discovery Learning Center", "Economics Building",
        "Mathematics Building", "Library",
        "Technology Learning Center", "Transportation Center"};

    private int count;

    public void generate() {
        List<String> usedNames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Building building = random(usedNames);
            buildingService.save(building);
            usedNames.add(building.getName());
        }
    }

    public static Building random() {
        return random(null);
    }

    public static Building random(List<String> usedNames) {
        List<String> randomNames = new ArrayList<>(Arrays.asList(ALL_NAMES));
        if (usedNames != null) {
            randomNames.removeAll(usedNames);
        }
        String name = Randomizer.random(randomNames);
        Building building = new Building();
        building.setName(name);
        building.setDescription("This is a " + name);
        return building;
    }

    public int getCount() {
        return count;
    }

    public BuildingGeneratorImpl setCount(int count) {
        if (count < 0 || count > 10) {
            throw new IllegalArgumentException("Parameter 'count' can't be less than zero or greater than 10!");
        }
        this.count = count;
        return this;
    }

}
