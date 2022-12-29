package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.generator.DataGenerator;
import org.example.service.RoleService;
import org.example.service.StudentService;
import org.example.utils.Randomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StudentGeneratorImpl implements DataGenerator {

    private final StudentService studentService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private Set<Role> roles;

    private static final String[] firstNames = {
        "Izzy", "Grier", "Frankie", "Terry", "Syd",
        "Neely", "Charley", "Laverne", "Lennie", "Kayden",
        "Sandy", "Francis", "Aston", "Haven", "Ripley",
        "Willie", "Bayley", "Lorin", "Arden", "Lorie"};

    private static final String[] lastNames = {
        "Mackenzie", "Kamryn", "Justy", "Cheyenne", "Brooklyn",
        "Emerson", "Arlie", "Nicky", "Dale", "Reagan",
        "Haze", "Chris", "Christie", "Leith", "Terry",
        "Skylar", "Coby", "Ryley", "Ellery", "Reign"};

    private int count;

    @PostConstruct
    private void postConstruct() {
        roles = new HashSet<>();
        Optional<Role> optionalRole = roleService.findByName("ROLE_STUDENT");
        optionalRole.ifPresent(role -> roles.add(role));
    }

    @Override
    public void generate() {
        List<Student> students = new ArrayList<>();
        Arrays.stream(firstNames).forEach(firstName -> Arrays.stream(lastNames).forEach(lastName -> {
            User user = new User();
            user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode("student"));
            user.setComment("Student account");

            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setUser(user);
            students.add(student);
        })
        );
        for (int i = 0; i < count; i++) {
            Student student = Randomizer.random(students);
            studentService.save(student);
            students.remove(student);
        }
    }

    public static Student random() {
        String firstName = Randomizer.random(firstNames);
        String lastName = Randomizer.random(lastNames);

        User user = new User();
        user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
        user.setPassword("student");
        user.setComment("Student account");
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setUser(user);
        return student;
    }

    public int getCount() {
        return count;
    }

    public StudentGeneratorImpl setCount(int count) {
        this.count = count;
        return this;
    }

}
