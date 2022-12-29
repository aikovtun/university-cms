package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Group;
import org.example.generator.impl.GroupGeneratorImpl;
import org.example.service.CourseService;
import org.example.service.GroupService;
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

@WebMvcTest(controllers = GroupController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private CourseService courseService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Group> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(groupService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/groups").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/groups"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Group> groups = List.of(
            GroupGeneratorImpl.random(),
            GroupGeneratorImpl.random(),
            GroupGeneratorImpl.random()
        );
        Page<Group> page = new PageImpl<>(groups, pageRequest, 1);
        when(groupService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/groups").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/groups").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/groups/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/group"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/groups/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Group group = GroupGeneratorImpl.random();
        when(groupService.findById(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/groups/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/group"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Group group = GroupGeneratorImpl.random();
        when(groupService.findById(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/groups/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("group", group));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups/edit - should return error 403")
    public void editShouldReturnError403() throws Exception {
        mockMvc.perform(get("/groups/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Group group = GroupGeneratorImpl.random();

        mockMvc.perform(post("/groups/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("group", group))
            .andDo(print())
            .andExpect(redirectedUrl("/groups?page=1&size=16"));

        verify(groupService).save(group);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups/create - should error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/groups/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Group group = GroupGeneratorImpl.random();

        mockMvc.perform(post("/groups/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("group", group))
            .andDo(print())
            .andExpect(redirectedUrl("/groups?page=1&size=16"));

        verify(groupService).save(group);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/groups/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/groups/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Group group = GroupGeneratorImpl.random();
        group.setId(1L);

        mockMvc.perform(post("/groups/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(group.getId()))
                .flashAttr("group", group))
            .andDo(print())
            .andExpect(redirectedUrl("/groups?page=1&size=16"));

        verify(groupService).deleteById(group.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/groups/delete - should return error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/groups/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
