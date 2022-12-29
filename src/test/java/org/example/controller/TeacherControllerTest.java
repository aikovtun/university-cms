package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Role;
import org.example.entity.Teacher;
import org.example.generator.impl.TeacherGeneratorImpl;
import org.example.service.RoleService;
import org.example.service.TeacherService;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@WebMvcTest(controllers = TeacherController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Teacher> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(teacherService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/teachers").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/teachers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Teacher> teachers = List.of(
            TeacherGeneratorImpl.random(),
            TeacherGeneratorImpl.random(),
            TeacherGeneratorImpl.random()
        );
        Page<Teacher> page = new PageImpl<>(teachers, pageRequest, 1);
        when(teacherService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/teachers").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/teachers").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/teachers/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/teacher"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/teachers/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Teacher teacher = TeacherGeneratorImpl.random();
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/teacher"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Teacher teacher = TeacherGeneratorImpl.random();
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("teacher", teacher));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers/edit - should return error 403")
    public void editAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/teachers/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Teacher teacher = TeacherGeneratorImpl.random();
        Role role = new Role("ROLE_TEACHER", "");
        when(roleService.findByName("ROLE_TEACHER")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/teachers/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("teacher", teacher))
            .andDo(print())
            .andExpect(redirectedUrl("/teachers?page=1&size=16"));

        verify(teacherService).save(teacher);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers/create - should error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/teachers/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Teacher teacher = TeacherGeneratorImpl.random();
        Role role = new Role("ROLE_TEACHER", "");
        when(roleService.findByName("ROLE_TEACHER")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/teachers/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("teacher", teacher))
            .andDo(print())
            .andExpect(redirectedUrl("/teachers?page=1&size=16"));

        verify(teacherService).save(teacher);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/teachers/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/teachers/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Teacher teacher = TeacherGeneratorImpl.random();
        teacher.setId(1L);

        mockMvc.perform(post("/teachers/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(teacher.getId()))
                .flashAttr("teacher", teacher))
            .andDo(print())
            .andExpect(redirectedUrl("/teachers?page=1&size=16"));

        verify(teacherService).deleteById(teacher.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/teachers/delete - should return error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/teachers/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
