package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Group;
import org.example.generator.DataGenerator;
import org.example.service.GroupService;
import org.example.utils.Randomizer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupGeneratorImpl implements DataGenerator {

    private final GroupService groupService;

    private static final List<Character> alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
        .mapToObj(ch -> (char) ch).collect(Collectors.toList());

    private int count;

    @Override
    public void generate(){
        for (int i = 0; i < count; i++) {
            groupService.save(random());
        }
    }

    public static Group random() {
        String name = "" + Randomizer.random(alphabet) + Randomizer.random(alphabet) + "-"
            + ThreadLocalRandom.current().nextInt(10, 99);
        Group group = new Group();
        group.setName(name);
        group.setDescription("This is a " + name);
        return group;
    }

    public int getCount() {
        return count;
    }

    public GroupGeneratorImpl setCount(int count) {
        this.count = count;
        return this;
    }

}
