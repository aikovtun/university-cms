package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.*;
import org.example.generator.DataGenerator;
import org.example.service.*;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class LessonGeneratorImpl implements DataGenerator {

    private final RoomService roomService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final LessonService lessonService;

    public void generate() {
        List<Room> rooms = roomService.findAll();
        List<Group> groups = groupService.findAll();
        List<Subject> subjects = subjectService.findAll();
        List<Teacher> teachers = teacherService.findAll();

        for (Room room : rooms) {
            LocalDate currentDate = getStartDate();
            LocalDate endDate = currentDate.plusDays(7);
            while (currentDate.isBefore(endDate)) {
                for (int number = 1; number <= 10; number++) {
                    Lesson lesson = new Lesson();
                    lesson.setRoom(room);
                    lesson.setGroup(randomGroup(groups));
                    lesson.setSubject(randomSubject(subjects));
                    lesson.setTeacher(randomTeacher(teachers));
                    lesson.setDate(currentDate);
                    lesson.setNumber(number);
                    lessonService.save(lesson);
                }
                currentDate =  currentDate.plusDays(1);
            }
        }
    }

    private Group randomGroup(List<Group> groups) {
        return groups.get(ThreadLocalRandom.current().nextInt(groups.size()));
    }

    private Subject randomSubject(List<Subject> subjects) {
        return subjects.get(ThreadLocalRandom.current().nextInt(subjects.size()));
    }

    private Teacher randomTeacher(List<Teacher> teachers) {
        return teachers.get(ThreadLocalRandom.current().nextInt(teachers.size()));
    }

    private LocalDate getStartDate() {
        LocalDate today = LocalDate.now();
        while (today.getDayOfWeek() != DayOfWeek.MONDAY) {
            today = today.minusDays(1);
        }
        return today;
    }

    public static Lesson random() {
        Lesson lesson = new Lesson();
        lesson.setRoom(RoomGeneratorImpl.random());
        lesson.setGroup(GroupGeneratorImpl.random());
        lesson.setSubject(SubjectGeneratorImpl.random());
        lesson.setTeacher(TeacherGeneratorImpl.random());
        lesson.setNumber(1 + ThreadLocalRandom.current().nextInt(9));
        lesson.setDate(LocalDate.now());
        return lesson;
    }


}
