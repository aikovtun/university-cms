package org.example.startup;

import lombok.RequiredArgsConstructor;
import org.example.generator.DataGeneratorManager;
import org.example.generator.impl.*;
import org.example.service.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("!test")
@Order(2)
@Transactional
@RequiredArgsConstructor
public class DataGeneratorRunner implements ApplicationRunner {

    private final TeacherService teacherService;
    private final BuildingGeneratorImpl buildingGenerator;
    private final RoomGeneratorImpl roomGenerator;
    private final TeacherGeneratorImpl teacherGenerator;
    private final StudentGeneratorImpl studentGenerator;
    private final CourseGeneratorImpl courseGenerator;
    private final GroupGeneratorImpl groupGenerator;
    private final SubjectGeneratorImpl subjectGenerator;
    private final StudentGroupAssignerGeneratorImpl studentGroupAssignerGenerator;
    private final GroupCourseAssignerGeneratorImpl groupCourseAssignerGenerator;
    private final LessonGeneratorImpl lessonGenerator;

    @Override
    public void run(ApplicationArguments args) {
        if (teacherService.findAll().isEmpty()) {
            DataGeneratorManager dataGeneratorManager = new DataGeneratorManager();
            dataGeneratorManager.addGenerator(buildingGenerator.setCount(5));
            dataGeneratorManager.addGenerator(roomGenerator.setCount(10));
            dataGeneratorManager.addGenerator(teacherGenerator.setCount(10));
            dataGeneratorManager.addGenerator(studentGenerator.setCount(200));
            dataGeneratorManager.addGenerator(courseGenerator.setCount(10));
            dataGeneratorManager.addGenerator(groupGenerator.setCount(100));
            dataGeneratorManager.addGenerator(subjectGenerator.setCount(25));
            dataGeneratorManager.addGenerator(studentGroupAssignerGenerator);
            dataGeneratorManager.addGenerator(groupCourseAssignerGenerator);
            dataGeneratorManager.addGenerator(lessonGenerator);
            dataGeneratorManager.generate();
        }
    }
}