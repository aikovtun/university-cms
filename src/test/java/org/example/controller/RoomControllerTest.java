package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Room;
import org.example.generator.impl.BuildingGeneratorImpl;
import org.example.generator.impl.RoomGeneratorImpl;
import org.example.service.BuildingService;
import org.example.service.RoomService;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BuildingService buildingService;

    @MockBean
    private RoomService roomService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Room> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(roomService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/rooms").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/rooms"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Room> rooms = List.of(
            RoomGeneratorImpl.random(),
            RoomGeneratorImpl.random(),
            RoomGeneratorImpl.random()
        );
        Page<Room> page = new PageImpl<>(rooms, pageRequest, 1);
        when(roomService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/rooms").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/rooms").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/rooms/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/room"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/rooms/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Room room = RoomGeneratorImpl.random();
        when(roomService.findById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(get("/rooms/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/room"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Room room = RoomGeneratorImpl.random();
        when(roomService.findById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(get("/rooms/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("room", room));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms/edit - should return error 403")
    public void editAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/rooms/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Room room = RoomGeneratorImpl.random();
        room.setBuilding(BuildingGeneratorImpl.random());

        mockMvc.perform(post("/rooms/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("room", room))
            .andDo(print())
            .andExpect(redirectedUrl("/rooms?page=1&size=16"));

        verify(roomService).save(room);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms/create - should return error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/rooms/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Room room = RoomGeneratorImpl.random();
        room.setBuilding(BuildingGeneratorImpl.random());

        mockMvc.perform(post("/rooms/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("room", room))
            .andDo(print())
            .andExpect(redirectedUrl("/rooms?page=1&size=16"));

        verify(roomService).save(room);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms/update - should return error 403")
    public void updateShouldError403() throws Exception {
        mockMvc.perform(post("/rooms/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/rooms/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Room room = RoomGeneratorImpl.random();
        room.setId(1L);

        mockMvc.perform(post("/rooms/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(room.getId()))
                .flashAttr("room", room))
            .andDo(print())
            .andExpect(redirectedUrl("/rooms?page=1&size=16"));

        verify(roomService).deleteById(room.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/rooms/delete - should return error 403")
    public void deleteShouldError403() throws Exception {
        mockMvc.perform(post("/rooms/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
