package org.example.controller;

import org.example.config.WebSecurityConfig;
import org.example.entity.Building;
import org.example.generator.impl.BuildingGeneratorImpl;
import org.example.service.BuildingService;
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

@WebMvcTest(controllers = BuildingController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(WebSecurityConfig.class)
public class BuildingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BuildingService buildingService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings - should return correct view")
    public void findAllShouldReturnCorrectView() throws Exception {
        Page<Building> page = new PageImpl<>(
            Collections.emptyList(), PageRequest.of(0, 25), 1);
        when(buildingService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/buildings").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("lists/buildings"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings - should return correct data")
    public void findAllShouldReturnCorrectData() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 25);
        List<Building> Buildings = List.of(
            BuildingGeneratorImpl.random(),
            BuildingGeneratorImpl.random(),
            BuildingGeneratorImpl.random()
        );
        Page<Building> page = new PageImpl<>(Buildings, pageRequest, 1);
        when(buildingService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/buildings").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("page", page));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings - should return error 403")
    public void findAllShouldReturnError403() throws Exception {
        mockMvc.perform(get("/buildings").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/add - should return correct view")
    public void addAShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/buildings/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/building"));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings/add - should return error 403")
    public void addAShouldReturnError403() throws Exception {
        mockMvc.perform(get("/buildings/add").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/edit - should return correct view")
    public void editAShouldReturnCorrectView() throws Exception {
        Building building = BuildingGeneratorImpl.random();
        when(buildingService.findById(1L)).thenReturn(Optional.of(building));

        mockMvc.perform(get("/buildings/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("edit/building"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/edit - should return correct data")
    public void editShouldReturnCorrectData() throws Exception {
        Building building = BuildingGeneratorImpl.random();
        when(buildingService.findById(1L)).thenReturn(Optional.of(building));

        mockMvc.perform(get("/buildings/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attribute("building", building));
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings/edit - should return error 403")
    public void editShouldReturnError403() throws Exception {
        mockMvc.perform(get("/buildings/edit").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", "1"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/create - should insert data to database")
    public void createShouldInsertDataToDatabase() throws Exception {
        Building building = BuildingGeneratorImpl.random();

        mockMvc.perform(post("/buildings/create").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("building", building))
            .andDo(print())
            .andExpect(redirectedUrl("/buildings?page=1&size=16"));

        verify(buildingService).save(building);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings/create - should return error 403")
    public void createShouldReturnError403() throws Exception {
        mockMvc.perform(post("/buildings/create").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/update - should update data in database")
    public void updateShouldUpdateDataInDatabase() throws Exception {
        Building building = BuildingGeneratorImpl.random();

        mockMvc.perform(post("/buildings/update").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .flashAttr("building", building))
            .andDo(print())
            .andExpect(redirectedUrl("/buildings?page=1&size=16"));

        verify(buildingService).save(building);
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings/update - should return error 403")
    public void updateShouldReturnError403() throws Exception {
        mockMvc.perform(post("/buildings/update").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/buildings/delete - should delete data from database")
    public void deleteShouldDeleteDataFromDatabase() throws Exception {
        Building building = BuildingGeneratorImpl.random();
        building.setId(1L);

        mockMvc.perform(post("/buildings/delete").with(csrf())
                .param("page", "1")
                .param("size", "16")
                .param("id", String.valueOf(building.getId()))
                .flashAttr("building", building))
            .andDo(print())
            .andExpect(redirectedUrl("/buildings?page=1&size=16"));

        verify(buildingService).deleteById(building.getId());
    }

    @Test
    @WithMockUser(roles = "{TEACHER, STUDENT}")
    @DisplayName("/buildings/delete - should error 403")
    public void deleteShouldReturnError403() throws Exception {
        mockMvc.perform(post("/buildings/delete").with(csrf())
                .param("page", "1")
                .param("size", "16"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

}
