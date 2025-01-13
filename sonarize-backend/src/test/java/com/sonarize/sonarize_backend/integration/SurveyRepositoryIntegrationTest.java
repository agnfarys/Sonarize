package com.sonarize.sonarize_backend.integration;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class SurveyRepositoryIntegrationTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    public void shouldFindSurveysByUserId() {
        List<Survey> surveys = surveyRepository.findByUserId("13d3f03d-1fb5-4027-ac90-d12dc44c08ba");
        assertEquals(7, surveys.size());
    }
}
