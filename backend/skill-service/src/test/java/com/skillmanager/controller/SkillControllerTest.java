
package com.skillmanager.controller;

import com.skillmanager.dto.request.CreateSkillRequest;
import com.skillmanager.dto.request.UpdateSkillRequest;
import com.skillmanager.dto.response.PageResponse;
import com.skillmanager.dto.response.SkillResponse;
import com.skillmanager.dto.response.SkillSuggestionResponse;
import com.skillmanager.model.SkillCategory;
import com.skillmanager.service.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    private static final String BASE_URL = "/api/v1/skills";

    private static final UUID SKILL_ID =
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SkillService skillService;

    private SkillResponse skillResponse() {
        return new SkillResponse(
                SKILL_ID,
                "Java",
                "java",
                SkillCategory.LANGUAGE,
                null,
                null
        );
    }

    private String toJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    @Test
    void createSkillShouldReturnCreatedWhenRequestIsValid() throws Exception {
        CreateSkillRequest request =
                new CreateSkillRequest("Java", SkillCategory.LANGUAGE);

        when(skillService.createSkill(any(CreateSkillRequest.class)))
                .thenReturn(skillResponse());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(SKILL_ID.toString()))
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.normalizedName").value("java"))
                .andExpect(jsonPath("$.category").value("LANGUAGE"));

        verify(skillService).createSkill(any(CreateSkillRequest.class));
    }

    @Test
    void getSkillsShouldReturnPaginatedResults() throws Exception {
        PageResponse<SkillResponse> response = new PageResponse<>(
                List.of(skillResponse()),
                0,
                20,
                1L,
                1
        );

        when(skillService.getSkills(
                eq(null),
                eq(null),
                any()
        )).thenReturn(response);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Java"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(skillService).getSkills(eq(null), eq(null), any());
    }

    @Test
    void getSkillsShouldAcceptSearchAndCategoryFilters() throws Exception {
        PageResponse<SkillResponse> response = new PageResponse<>(
                List.of(skillResponse()),
                0,
                10,
                1L,
                1
        );

        when(skillService.getSkills(
                eq("jav"),
                eq(SkillCategory.LANGUAGE),
                any()
        )).thenReturn(response);

        mockMvc.perform(get(BASE_URL)
                        .param("search", "jav")
                        .param("category", "LANGUAGE")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Java"));

        verify(skillService).getSkills(
                eq("jav"),
                eq(SkillCategory.LANGUAGE),
                any()
        );
    }

    @Test
    void getSuggestionsShouldReturnMatchingSkills() throws Exception {
        when(skillService.getSuggestions("spr"))
                .thenReturn(List.of(
                        new SkillSuggestionResponse(
                                SKILL_ID,
                                "Spring Boot",
                                SkillCategory.FRAMEWORK
                        )
                ));

        mockMvc.perform(get(BASE_URL + "/suggestions")
                        .param("q", "spr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(SKILL_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Spring Boot"))
                .andExpect(jsonPath("$[0].category").value("FRAMEWORK"));

        verify(skillService).getSuggestions("spr");
    }

    @Test
    void getSkillByIdShouldReturnSkill() throws Exception {
        when(skillService.getSkillById(SKILL_ID))
                .thenReturn(skillResponse());

        mockMvc.perform(get(BASE_URL + "/" + SKILL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SKILL_ID.toString()))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(skillService).getSkillById(SKILL_ID);
    }

    @Test
    void updateSkillShouldReturnUpdatedSkill() throws Exception {
        UpdateSkillRequest request =
                new UpdateSkillRequest("Java", SkillCategory.LANGUAGE);

        when(skillService.updateSkill(
                eq(SKILL_ID),
                any(UpdateSkillRequest.class)
        )).thenReturn(skillResponse());

        mockMvc.perform(put(BASE_URL + "/" + SKILL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SKILL_ID.toString()))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(skillService).updateSkill(
                eq(SKILL_ID),
                any(UpdateSkillRequest.class)
        );
    }

    @Test
    void deleteSkillShouldReturnNoContent() throws Exception {
        doNothing().when(skillService).deleteSkill(SKILL_ID);

        mockMvc.perform(delete(BASE_URL + "/" + SKILL_ID))
                .andExpect(status().isNoContent());

        verify(skillService).deleteSkill(SKILL_ID);
    }

    @Test
    void getSkillByIdShouldReturnBadRequestForInvalidUuid() throws Exception {
        mockMvc.perform(get(BASE_URL + "/not-a-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSkillsShouldReturnBadRequestForInvalidCategory() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("category", "INVALID_CATEGORY"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSuggestionsShouldReturnBadRequestWhenQueryIsMissing() throws Exception {
        mockMvc.perform(get(BASE_URL + "/suggestions"))
                .andExpect(status().isBadRequest());
    }
}
