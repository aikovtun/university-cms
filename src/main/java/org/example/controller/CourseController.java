package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Course;
import org.example.service.CourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "16") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("title", "Courses");
        model.addAttribute("page", courseService.findAll(pageRequest));
        return "lists/courses";
    }

    @GetMapping("/add")
    public String add(Model model,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "16") Integer size) {
        if (!model.containsAttribute("course")) {
            Course course = new Course();
            model.addAttribute("course", course);
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", "New course");
        return "edit/course";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "16") Integer size,
                       @RequestParam Long id) {
        if (!model.containsAttribute("course")) {
            Optional<Course> optionalCourse = courseService.findById(id);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                model.addAttribute("course", course);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        String presentation = String.format("Course (id=%d)", id);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", presentation);
        return "edit/course";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("course") Course course,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return add(model, page, size);
        }
        courseService.save(course);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/courses";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("course") Course course,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return edit(model, page, size, course.getId());
        }
        courseService.save(course);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/courses";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        courseService.deleteById(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/courses";
    }

}
