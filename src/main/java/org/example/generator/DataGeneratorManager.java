package org.example.generator;

import java.util.ArrayList;
import java.util.List;

public class DataGeneratorManager {

    private final List<DataGenerator> generators = new ArrayList<>();

    public void generate() {
        for (DataGenerator generator : generators) {
            generator.generate();
        }
    }

    public void addGenerator(DataGenerator generator) {
        generators.add(generator);
    }

    public void removeGenerator(DataGenerator generator) {
        generators.remove(generator);
    }

}
