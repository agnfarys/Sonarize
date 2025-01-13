package com.sonarize.sonarize_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonarize.sonarize_backend.controller.SurveyController;
import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.service.SurveyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCreateSurvey() throws Exception {
        Survey survey = new Survey();
        survey.setUserId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
        survey.setMood("happy");
        survey.setGenres(List.of("pop"));
        survey.setEnergyLevel("moderate");
        survey.setOccasion("workout");
        survey.setFavoriteArtists(List.of("Imagine Dragons"));
        survey.setDiscoveryPreference("new releases");
        survey.setLanguagePreference("english");
        survey.setPlaylistLength("10");

        when(surveyService.createSurvey(any(Survey.class))).thenReturn(survey);

        mockMvc.perform(post("/api/surveys/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8"))
                .andExpect(jsonPath("$.mood").value("happy"));

        verify(surveyService, times(1)).createSurvey(any(Survey.class));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetSurveysByUserId() throws Exception {
        String userId = "5ef6aa61-9327-458f-9ad2-0dba4bed5ab8";
        Survey survey = new Survey();
        survey.setUserId(userId);

        when(surveyService.getSurveyByUserId(userId)).thenReturn(List.of(survey));

        mockMvc.perform(get("/api/surveys/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId));

        verify(surveyService, times(1)).getSurveyByUserId(userId);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetSurveysByUserIdNotFound() throws Exception {
        String userId = "5ef6aa61-9327-458f-9ad2-0dba4bed5ab8";

        when(surveyService.getSurveyByUserId(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/surveys/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(surveyService, times(1)).getSurveyByUserId(userId);
    }

}
