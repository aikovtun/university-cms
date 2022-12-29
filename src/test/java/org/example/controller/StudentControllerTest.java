package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Role;
import org.example.entity.Student;
import org.example.generator.impl.StudentGeneratorImpl;
import org.example.service.GroupService;
import org.example.service.RoleService;
import org.example.service.StudentService;
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

@WebMvcTest(controllers = StudentController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Student> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(studentService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/students").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/students"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Student> students = List.of(
            StudentGeneratorImpl.random(),
            StudentGeneratorImpl.random(),
            StudentGeneratorImpl.random()
        );
        Page<Student> page = new PageImpl<>(students, pageRequest, 1);
        when(studentService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/students").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/students").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/students/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/student"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/students/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Student student = StudentGeneratorImpl.random();
        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/student"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Student student = StudentGeneratorImpl.random();
        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("student", student));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students/edit - should return error 403")
    public void editAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/students/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Student student = StudentGeneratorImpl.random();
        Role role = new Role("ROLE_STUDENT", "");
        when(roleService.findByName("ROLE_STUDENT")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/students/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("student", student))
            .andDo(print())
            .andExpect(redirectedUrl("/students?page=1&size=16"));

        verify(studentService).save(student);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students/create - should return error 403")
    public void createShouldReturn403() throws Exception {
        mockMvc.perform(post("/students/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Student student = StudentGeneratorImpl.random();
        Role role = new Role("ROLE_STUDENT", "");
        when(roleService.findByName("ROLE_STUDENT")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/students/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("student", student))
            .andDo(print())
            .andExpect(redirectedUrl("/students?page=1&size=16"));

        verify(studentService).save(student);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/students/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/students/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Student student = StudentGeneratorImpl.random();
        student.setId(1L);

        mockMvc.perform(post("/students/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(student.getId()))
                .flashAttr("student", student))
            .andDo(print())
            .andExpect(redirectedUrl("/students?page=1&size=16"));

        verify(studentService).deleteById(student.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/students/delete - should return error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/students/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
