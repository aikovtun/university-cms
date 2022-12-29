package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Course;
import org.example.entity.Group;
import org.example.generator.DataGenerator;
import org.example.service.CourseService;
import org.example.service.GroupService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class GroupCourseAssignerGeneratorImpl implements DataGenerator {

    private final GroupService groupService;
    private final CourseService courseService;

    @Override
    public void generate() {
        List<Group> groups = groupService.findAll();
        List<Course> courses = courseService.findAll();
        for (Group group : groups) {
            Course course = randomCourse(courses);
            group.setCourse(course);
            groupService.save(group);
        }
    }

    private Course randomCourse(List<Course> courses) {
        return courses.get(ThreadLocalRandom.current().nextInt(courses.size()));
    }

}
