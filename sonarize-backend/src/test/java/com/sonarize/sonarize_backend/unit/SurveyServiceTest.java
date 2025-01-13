package com.sonarize.sonarize_backend.unit;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.repository.SurveyRepository;
import com.sonarize.sonarize_backend.service.SurveyService;
import com.sonarize.sonarize_backend.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void testGetSurveyByUserId() {
        Survey testSurvey = TestDataUtil.createTestSurvey();
        when(surveyRepository.findByUserId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8"))
                .thenReturn(List.of(testSurvey));

        List<Survey> surveys = surveyService.getSurveyByUserId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");

        assertEquals(1, surveys.size());
        assertEquals("happy", surveys.get(0).getMood());
        verify(surveyRepository, times(1)).findByUserId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
    }
}
