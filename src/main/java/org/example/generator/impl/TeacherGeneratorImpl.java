package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.Teacher;
import org.example.entity.User;
import org.example.generator.DataGenerator;
import org.example.service.RoleService;
import org.example.service.TeacherService;
import org.example.utils.Randomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TeacherGeneratorImpl implements DataGenerator {

    private final TeacherService teacherService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private Set<Role> roles;

    private static final String[] firstNames = {
        "Kiran", "Adeola", "Agam", "Reagan", "Arya",
        "Peťa", "Steph", "Amal", "Hosni", "Dayo",
        "Shachar", "Jadyn", "Golzar", "Tam", "Gul",
        "Duru", "Buhle", "Chimwala", "Fungai", "Lacey"};

    private static final String[] lastNames = {
        "Esen", "Brook", "Pleun", "Katleho", "Katlego",
        "Zohar", "Andile", "Tionge", "Metztli", "Harinder",
        "Chijindum", "Adedayo", "Pitsiulaaq", "Navdeep", "Addison",
        "Oghenekevwe", "Tiwonge", "Izzy", "Phoenix", "Rayan"};

    private int count;

    @PostConstruct
    private void postConstruct() {
        roles = new HashSet<>();
        Optional<Role> optionalRole = roleService.findByName("ROLE_TEACHER");
        optionalRole.ifPresent(role -> roles.add(role));
    }

    @Override
    public void generate() {
        List<Teacher> teachers = new ArrayList<>();
        Arrays.stream(firstNames).forEach(firstName -> Arrays.stream(lastNames).forEach(lastName -> {
            User user = new User();
            user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode("teacher"));
            user.setComment("Teacher account");

            Teacher teacher = new Teacher();
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setUser(user);
            teachers.add(teacher);
        })
        );
        for (int i = 0; i < count; i++) {
            Teacher teacher = Randomizer.random(teachers);
            teacherService.save(teacher);
            teachers.remove(teacher);
        }
    }

    public static Teacher random() {
        String firstName = Randomizer.random(firstNames);
        String lastName = Randomizer.random(lastNames);

        User user = new User();
        user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
        user.setPassword("teacher");
        user.setComment("Teacher account");
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setUser(user);
        return teacher;
    }

    public int getCount() {
        return count;
    }

    public TeacherGeneratorImpl setCount(int count) {
        this.count = count;
        return this;
    }

}
