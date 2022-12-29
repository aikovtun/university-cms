package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Course;
import org.example.generator.DataGenerator;
import org.example.service.CourseService;
import org.example.utils.Randomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseGeneratorImpl implements DataGenerator {

    private final CourseService courseService;

    private static final String[] ALL_NAMES = {
        "Agriculture", "Farm Management", "Horticulture", "Plant and Crop Sciences", "Veterinary Medicine",
        "Astronomy", "Biology", "Environmental Sciences", "Materials Sciences", "Mathematics",
        "Accounting", "Business Studies", "E-Commerce", "Office Administration", "Marketing",
        "Computer Science", "Computing", "Multimedia", "Software", "IT",
        "Aerospace Engineering", "Biomedical Engineering", "Electronic Engineering", "Quality Control", "Telecommunications"};

    private int count;

    public void generate() {
        List<String> usedNames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Course course = random(usedNames);
            courseService.save(course);
            usedNames.add(course.getName());
        }
    }

    public static Course random() {
        return random(null);
    }

    public static Course random(List<String> usedNames) {
        List<String> randomNames = new ArrayList<>(Arrays.asList(ALL_NAMES));
        if (usedNames != null) {
            randomNames.removeAll(usedNames);
        }
        String name = Randomizer.random(randomNames);
        Course course = new Course();
        course.setName(name);
        course.setDescription("This is a " + name);
        return course;
    }

    public int getCount() {
        return count;
    }

    public CourseGeneratorImpl setCount(int count) {
        if (count < 0 || count > 25) {
            throw new IllegalArgumentException("Parameter 'count' can't be less than zero or greater than 25!");
        }
        this.count = count;
        return this;
    }

}
