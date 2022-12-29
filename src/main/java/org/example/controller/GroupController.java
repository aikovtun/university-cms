package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Group;
import org.example.service.CourseService;
import org.example.service.GroupService;
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
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final CourseService courseService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "16") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("title", "Group");
        model.addAttribute("page", groupService.findAll(pageRequest));
        return "lists/groups";
    }

    @GetMapping("/add")
    public String add(Model model,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "16") Integer size) {
        if (!model.containsAttribute("group")) {
            Group group = new Group();
            model.addAttribute("group", group);
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", "New group");
        model.addAttribute("courses", courseService.findAll());
        return "edit/group";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "16") Integer size,
                       @RequestParam Long id) {
        if (!model.containsAttribute("group")) {
            Optional<Group> optionalGroup = groupService.findById(id);
            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                model.addAttribute("group", group);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        String presentation = String.format("Group (id=%d)", id);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", presentation);
        model.addAttribute("courses", courseService.findAll());
        return "edit/group";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("group") Group group,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return add(model, page, size);
        }
        groupService.save(group);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/groups";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("group") Group group,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return edit(model, page, size, group.getId());
        }
        groupService.save(group);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/groups";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        groupService.deleteById(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/groups";
    }

}
