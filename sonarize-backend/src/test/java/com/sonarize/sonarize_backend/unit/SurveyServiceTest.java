package com.sonarize.sonarize_backend.unit;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.repository.SurveyRepository;
import com.sonarize.sonarize_backend.service.SurveyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyService surveyService;

    public SurveyServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSurvey() {
        Survey survey = new Survey();
        survey.setUserId("123");
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey createdSurvey = surveyService.createSurvey(survey);

        assertEquals("123", createdSurvey.getUserId());
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    void testGetSurveyByUserId() {
        String userId = "123";
        List<Survey> mockSurveys = List.of(new Survey());
        when(surveyRepository.findByUserId(userId)).thenReturn(mockSurveys);

        List<Survey> surveys = surveyService.getSurveyByUserId(userId);

        assertEquals(1, surveys.size());
        verify(surveyRepository, times(1)).findByUserId(userId);
    }
}
