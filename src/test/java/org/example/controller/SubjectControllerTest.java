package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Subject;
import org.example.generator.impl.SubjectGeneratorImpl;
import org.example.service.SubjectService;
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

@WebMvcTest(controllers = SubjectController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SubjectService subjectService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Subject> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(subjectService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/subjects").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/subjects"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Subject> subjects = List.of(
            SubjectGeneratorImpl.random(),
            SubjectGeneratorImpl.random(),
            SubjectGeneratorImpl.random()
        );
        Page<Subject> page = new PageImpl<>(subjects, pageRequest, 1);
        when(subjectService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/subjects").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/subjects").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/subjects/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/subject"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/subjects/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Subject subject = SubjectGeneratorImpl.random();
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/subject"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Subject subject = SubjectGeneratorImpl.random();
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("subject", subject));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects/edit - should return error 403")
    public void editAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/subjects/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Subject subject = SubjectGeneratorImpl.random();

        mockMvc.perform(post("/subjects/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("subject", subject))
            .andDo(print())
            .andExpect(redirectedUrl("/subjects?page=1&size=16"));

        verify(subjectService).save(subject);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects/create - should return 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/subjects/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Subject subject = SubjectGeneratorImpl.random();

        mockMvc.perform(post("/subjects/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("subject", subject))
            .andDo(print())
            .andExpect(redirectedUrl("/subjects?page=1&size=16"));

        verify(subjectService).save(subject);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/subjects/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/subjects/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Subject subject = SubjectGeneratorImpl.random();
        subject.setId(1L);

        mockMvc.perform(post("/subjects/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(subject.getId()))
                .flashAttr("subject", subject))
            .andDo(print())
            .andExpect(redirectedUrl("/subjects?page=1&size=16"));

        verify(subjectService).deleteById(subject.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/subjects/delete - should error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/subjects/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
