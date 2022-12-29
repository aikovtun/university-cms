package org.example.generator.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Administrator;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.generator.DataGenerator;
import org.example.service.AdministratorService;
import org.example.service.RoleService;
import org.example.utils.Randomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@RequiredArgsConstructor
public class AdministratorGeneratorImpl implements DataGenerator {

    private final AdministratorService administratorService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private Set<Role> roles;

    private static final String[] firstNames = {
        "Taha", "Juliet", "Belle", "Kira", "Nora",
        "Sara", "Erin", "Lyndon", "Julia", "Cian",
        "Darcy", "Felix", "Kasey", "Seren", "Courtney",
        "Mattie", "Phoenix", "Verity", "Haydn", "Morgan"};

    private static final String[] lastNames = {
        "Black", "Nelson", "Wang", "Reynolds", "Mason",
        "Jacobs", "Barlow", "Willis", "Finch", "Carpenter",
        "Howard", "Collins", "Ramos", "Berry", "Wu",
        "Cisneros", "Medina", "Clay", "Decker", "Fowler"};

    private int count;

    @PostConstruct
    private void postConstruct() {
        roles = new HashSet<>();
        Optional<Role> optionalRole = roleService.findByName("ROLE_ADMIN");
        optionalRole.ifPresent(role -> roles.add(role));
    }

    @Override
    public void generate() {
        List<Administrator> administrators = new ArrayList<>();
        Arrays.stream(firstNames).forEach(firstName -> Arrays.stream(lastNames).forEach(lastName -> {
            User user = new User();
            user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode("root"));
            user.setComment("Administrator account");

            Administrator administrator = new Administrator();
            administrator.setFirstName(firstName);
            administrator.setLastName(lastName);
            administrator.setUser(user);
            administrators.add(administrator);
        })
        );
        for (int i = 0; i < count; i++) {
            Administrator administrator = Randomizer.random(administrators);
            administratorService.save(administrator);
            administrators.remove(administrator);
        }
    }

    public static Administrator random() {
        String firstName = Randomizer.random(firstNames);
        String lastName = Randomizer.random(lastNames);

        User user = new User();
        user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.local");
        user.setPassword("root");
        user.setComment("Administrator account");
        Administrator administrator = new Administrator();
        administrator.setFirstName(firstName);
        administrator.setLastName(lastName);
        administrator.setUser(user);
        return administrator;
    }

    public int getCount() {
        return count;
    }

    public AdministratorGeneratorImpl setCount(int count) {
        this.count = count;
        return this;
    }

}
