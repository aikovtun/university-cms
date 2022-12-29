package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.Teacher;
import org.example.entity.User;
import org.example.service.RoleService;
import org.example.service.TeacherService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "16") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("title", "Teachers");
        model.addAttribute("page", teacherService.findAll(pageRequest));
        return "lists/teachers";
    }

    @GetMapping("/add")
    public String add(Model model,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "16") Integer size) {
        if (!model.containsAttribute("teacher")) {
            Teacher teacher = new Teacher();
            teacher.setUser(new User());
            model.addAttribute("teacher", teacher);
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", "New teacher");
        return "edit/teacher";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "16") Integer size,
                       @RequestParam Long id) {
        if (!model.containsAttribute("teacher")) {
            Optional<Teacher> optionalTeacher = teacherService.findById(id);
            if (optionalTeacher.isPresent()) {
                Teacher teacher = optionalTeacher.get();
                model.addAttribute("teacher", teacher);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        String presentation = String.format("Teacher (id=%d)", id);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", presentation);
        return "edit/teacher";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("teacher") Teacher teacher,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return add(model, page, size);
        }
        User user = teacher.getUser();
        Optional<Role> OptionalDefaultRole = roleService.findByName("ROLE_TEACHER");
        OptionalDefaultRole.ifPresent(user::addRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        teacherService.save(teacher);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/teachers";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("teacher") Teacher teacher,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return edit(model, page, size, teacher.getId());
        }
        User user = teacher.getUser();
        if (!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        teacherService.save(teacher);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/teachers";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        teacherService.deleteById(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/teachers";
    }

}
