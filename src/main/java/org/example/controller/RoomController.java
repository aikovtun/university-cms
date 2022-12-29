package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Room;
import org.example.entity.RoomType;
import org.example.service.BuildingService;
import org.example.service.RoomService;
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
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final BuildingService buildingService;

    @GetMapping
    public String findAll(Model model,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "16") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("title", "Rooms");
        model.addAttribute("page", roomService.findAll(pageRequest));
        return "lists/rooms";
    }

    @GetMapping("/add")
    public String add(Model model,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "16") Integer size) {
        if (!model.containsAttribute("room")) {
            Room room = new Room();
            model.addAttribute("room", room);
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", "New room");
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("types", RoomType.values());
        return "edit/room";
    }

    @GetMapping("/edit")
    public String edit(Model model,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "16") Integer size,
                       @RequestParam Long id) {
        if (!model.containsAttribute("room")) {
            Optional<Room> optionalRoom = roomService.findById(id);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                model.addAttribute("room", room);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        String presentation = String.format("Room (id=%d)", id);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("title", presentation);
        model.addAttribute("buildings", buildingService.findAll());
        model.addAttribute("types", RoomType.values());
        return "edit/room";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("room") Room room,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return add(model, page, size);
        }
        roomService.save(room);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/rooms";
    }

    @PostMapping("/update")
    public String update(Model model,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @Valid @ModelAttribute("room") Room room,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return edit(model, page, size, room.getId());
        }
        roomService.save(room);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/rooms";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "16") Integer size,
                         @RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        roomService.deleteById(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/rooms";
    }

}
