package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Subject;
import org.example.generator.DataGenerator;
import org.example.service.SubjectService;
import org.example.utils.Randomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubjectGeneratorImpl implements DataGenerator {

    private final SubjectService subjectService;

    private static final String[] ALL_NAMES = {
        "English", "Maths", "Science", "Physical", "Basics",
        "Arts", "History", "Literature", "Philosophy", "Anthropology",
        "Archaeology", "Economics", "Geography", "Psychology", "Sociology",
        "Chemistry", "Physics", "Logic", "Statistics", "Business",
        "Engineering", "Law", "Transportation", "Journalism", "Divinity"};

    private int count;

    public void generate() {
        List<String> usedNames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Subject subject = random(usedNames);
            subjectService.save(subject);
            usedNames.add(subject.getName());
        }
    }

    public static Subject random() {
        return random(null);
    }

    public static Subject random(List<String> usedNames) {
        List<String> randomNames = new ArrayList<>(Arrays.asList(ALL_NAMES));
        if (usedNames != null) {
            randomNames.removeAll(usedNames);
        }
        String name = Randomizer.random(randomNames);
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription("This is a " + name);
        return subject;
    }

    public int getCount() {
        return count;
    }

    public SubjectGeneratorImpl setCount(int count) {
        if (count < 0 || count > 25) {
            throw new IllegalArgumentException("Parameter 'count' can't be less than zero or greater than 25!");
        }
        this.count = count;
        return this;
    }

}
