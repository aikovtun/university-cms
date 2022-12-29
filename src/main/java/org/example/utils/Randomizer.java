package org.example.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    public static <T> T random(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    public static <T> T random(List<T> array) {
        return array.get(ThreadLocalRandom.current().nextInt(array.size()));
    }

}
