package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Administrator;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.service.AdministratorService;
import org.example.service.RoleService;
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
@RequestMapping("administrators")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @GetMapping
    public String findAll(Model model,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "16") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("title", "Administrators");
        model.addAttribute("page", administratorService.findAll(pageRequest));
        return "lists/administrators";
    }

    @GetMapping("/add")
    public String add(Model model,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "16") Integer size) {
        if (!model.containsAttribute("administrator")) {
            Administrator administrator = new Administrator();
            administrator.setUser(new User());
            model.addAttribute("administrator", administrator);
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", "New administrator");
        return "edit/administrator";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "16") Integer size,
                       @RequestParam Long id) {
        if (!model.containsAttribute("administrator")) {
            Optional<Administrator> optionalAdministrator = administratorService.findById(id);
            if (optionalAdministrator.isPresent()) {
                Administrator administrator = optionalAdministrator.get();
                model.addAttribute("administrator", administrator);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        String presentation = String.format("Administrator (id=%d)", id);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", presentation);
        return "edit/administrator";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("administrator") Administrator administrator,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return add(model, page, size);
        }
        User user = administrator.getUser();
        Optional<Role> OptionalDefaultRole = roleService.findByName("ROLE_ADMIN");
        OptionalDefaultRole.ifPresent(user::addRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        administratorService.save(administrator);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/administrators";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("administrator") Administrator administrator,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return edit(model, page, size, administrator.getId());
        }
        User user = administrator.getUser();
        if (!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        administratorService.save(administrator);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/administrators";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        administratorService.deleteById(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/administrators";
    }

}
