package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Lesson;
import org.example.generator.impl.LessonGeneratorImpl;
import org.example.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

@WebMvcTest(controllers = LessonController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private SubjectService subjectService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private RoomService roomService;

    @Test
    @WithMockUser(roles = "ADMIN, TEACHER, STUDENT")
    @DisplayName("/lessons - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Lesson> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(lessonService.findAllByCurrentUser(any())).thenReturn(page);

        mockMvc.perform(get("/lessons").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/lessons"));
    }

    @Test
    @WithMockUser(roles = "ADMIN, TEACHER, STUDENT")
    @DisplayName("/lessons - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Lesson> lessons = List.of(
            LessonGeneratorImpl.random(),
            LessonGeneratorImpl.random(),
            LessonGeneratorImpl.random()
        );
        Page<Lesson> page = new PageImpl<>(lessons, pageRequest, 1);
        when(lessonService.findAllByCurrentUser(any())).thenReturn(page);

        mockMvc.perform(get("/lessons").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/lessons/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/lesson"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/lessons/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/lessons/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Lesson lesson = LessonGeneratorImpl.random();
        when(lessonService.findById(1L)).thenReturn(Optional.of(lesson));

        mockMvc.perform(get("/lessons/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/lesson"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Lesson lesson = LessonGeneratorImpl.random();
        when(lessonService.findById(1L)).thenReturn(Optional.of(lesson));

        mockMvc.perform(get("/lessons/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("lesson", lesson));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/lessons/edit - should return error 403")
    public void editAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/lessons/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Lesson lesson = LessonGeneratorImpl.random();

        mockMvc.perform(post("/lessons/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("lesson", lesson))
            .andDo(print())
            .andExpect(redirectedUrl("/lessons?page=1&size=16"));

        verify(lessonService).save(lesson);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/lessons/create - should return error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/lessons/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Lesson lesson = LessonGeneratorImpl.random();

        mockMvc.perform(post("/lessons/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("lesson", lesson))
            .andDo(print())
            .andExpect(redirectedUrl("/lessons?page=1&size=16"));

        verify(lessonService).save(lesson);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/lessons/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/lessons/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/lessons/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Lesson lesson = LessonGeneratorImpl.random();
        lesson.setId(1L);

        mockMvc.perform(post("/lessons/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(lesson.getId()))
                .flashAttr("lesson", lesson))
            .andDo(print())
            .andExpect(redirectedUrl("/lessons?page=1&size=16"));

        verify(lessonService).deleteById(lesson.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/lessons/delete - should return error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/lessons/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
