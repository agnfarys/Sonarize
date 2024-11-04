package com.sonarize.sonarize_backend.service;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey createSurvey(Survey playlist) {
        playlist.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return surveyRepository.save(playlist);
    }

    public List<Survey> getSurveyByUserId(String userId) {
        return surveyRepository.findByUserId(userId);
    }
}
