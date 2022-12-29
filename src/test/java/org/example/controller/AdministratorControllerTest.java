package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Administrator;
import org.example.entity.Role;
import org.example.generator.impl.AdministratorGeneratorImpl;
import org.example.service.AdministratorService;
import org.example.service.RoleService;
import org.example.service.UserService;
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

@WebMvcTest(controllers = AdministratorController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class AdministratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministratorService administratorService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Administrator> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(administratorService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/administrators").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/administrators"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Administrator> administrators = List.of(
            AdministratorGeneratorImpl.random(),
            AdministratorGeneratorImpl.random(),
            AdministratorGeneratorImpl.random()
        );
        Page<Administrator> page = new PageImpl<>(administrators, pageRequest, 1);
        when(administratorService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/administrators").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/administrators").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/administrators/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/administrator"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Administrator administrator = AdministratorGeneratorImpl.random();
        when(administratorService.findById(1L)).thenReturn(Optional.of(administrator));

        mockMvc.perform(get("/administrators/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/administrator"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/administrators/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Administrator administrator = AdministratorGeneratorImpl.random();
        when(administratorService.findById(1L)).thenReturn(Optional.of(administrator));

        mockMvc.perform(get("/administrators/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("administrator", administrator));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators/edit - should return error 403")
    public void editShouldReturnError403() throws Exception {
        mockMvc.perform(get("/administrators/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Administrator administrator = AdministratorGeneratorImpl.random();
        Role role = new Role("ROLE_ADMIN", "");
        when(roleService.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/administrators/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("administrator", administrator))
            .andDo(print())
            .andExpect(redirectedUrl("/administrators?page=1&size=16"));

        verify(administratorService).save(administrator);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators/create - should return error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/administrators/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Administrator administrator = AdministratorGeneratorImpl.random();
        Role role = new Role("ROLE_ADMIN", "");
        when(administratorService.findById(any())).thenReturn(Optional.of(administrator));
        when(userService.findById(any())).thenReturn(Optional.of(administrator.getUser()));
        when(roleService.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        mockMvc.perform(post("/administrators/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("administrator", administrator))
            .andDo(print())
            .andExpect(redirectedUrl("/administrators?page=1&size=16"));

        verify(administratorService).save(administrator);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/administrators/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/administrators/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Administrator administrator = AdministratorGeneratorImpl.random();
        administrator.setId(1L);

        mockMvc.perform(post("/administrators/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(administrator.getId()))
                .flashAttr("administrator", administrator))
            .andDo(print())
            .andExpect(redirectedUrl("/administrators?page=1&size=16"));

        verify(administratorService).deleteById(administrator.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/administrators/delete - should return error 403")
    public void deleteShouldReturn403() throws Exception {
        mockMvc.perform(post("/administrators/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
