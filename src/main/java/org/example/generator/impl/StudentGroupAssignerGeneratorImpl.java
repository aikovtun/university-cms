package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Group;
import org.example.entity.Student;
import org.example.generator.DataGenerator;
import org.example.service.GroupService;
import org.example.service.StudentService;
import org.example.utils.Randomizer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentGroupAssignerGeneratorImpl implements DataGenerator {

    private final StudentService studentService;
    private final GroupService groupService;

    @Override
    public void generate() {
        List<Student> students = studentService.findAll();
        List<Group> groups = groupService.findAll();
        for (Student student : students) {
            Group group = randomGroup(groups);
            student.setGroup(group);
            studentService.save(student);
        }
    }

    private Group randomGroup(List<Group> groups) {
        return Randomizer.random(groups);
    }

}
