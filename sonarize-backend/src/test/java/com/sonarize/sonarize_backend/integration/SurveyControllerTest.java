package com.sonarize.sonarize_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonarize.sonarize_backend.controller.SurveyController;
import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.service.SurveyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    public SurveyControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSurvey() throws Exception {
        Survey survey = new Survey();
        survey.setUserId("123");

        when(surveyService.createSurvey(any(Survey.class))).thenReturn(survey);

        mockMvc.perform(post("/api/surveys/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("123"));

        verify(surveyService, times(1)).createSurvey(any(Survey.class));
    }

    @Test
    void testGetSurveysByUserId() throws Exception {
        String userId = "123";
        when(surveyService.getSurveyByUserId(userId)).thenReturn(List.of(new Survey()));

        mockMvc.perform(get("/api/surveys/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(surveyService, times(1)).getSurveyByUserId(userId);
    }
}
