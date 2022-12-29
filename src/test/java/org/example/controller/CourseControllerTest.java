package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Course;
import org.example.generator.impl.CourseGeneratorImpl;
import org.example.service.CourseService;
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

@WebMvcTest(controllers = CourseController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CourseService courseService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Course> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(courseService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/courses").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/courses"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Course> courses = List.of(
            CourseGeneratorImpl.random(),
            CourseGeneratorImpl.random(),
            CourseGeneratorImpl.random()
        );
        Page<Course> page = new PageImpl<>(courses, pageRequest, 1);
        when(courseService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/courses").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/courses").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/courses/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/course"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/courses/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Course course = CourseGeneratorImpl.random();
        when(courseService.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/courses/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/course"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Course course = CourseGeneratorImpl.random();
        when(courseService.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/courses/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("course", course));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses/edit - should return error 403")
    public void editShouldReturnError403() throws Exception {
        mockMvc.perform(get("/courses/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Course course = CourseGeneratorImpl.random();

        mockMvc.perform(post("/courses/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("course", course))
            .andDo(print())
            .andExpect(redirectedUrl("/courses?page=1&size=16"));

        verify(courseService).save(course);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses/create - should return error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/courses/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Course course = CourseGeneratorImpl.random();

        mockMvc.perform(post("/courses/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("course", course))
            .andDo(print())
            .andExpect(redirectedUrl("/courses?page=1&size=16"));

        verify(courseService).save(course);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/courses/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/courses/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Course course = CourseGeneratorImpl.random();
        course.setId(1L);

        mockMvc.perform(post("/courses/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(course.getId()))
                .flashAttr("course", course))
            .andDo(print())
            .andExpect(redirectedUrl("/courses?page=1&size=16"));

        verify(courseService).deleteById(course.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/courses/delete - should return error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/courses/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
