package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Survey;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.sonarize.sonarize_backend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/create")
    public Survey createSurvey(@RequestBody Survey survey) {
        return surveyService.createSurvey(survey);
    }

    @GetMapping("/user/{userId}")
    public List<Survey> getSurveysByUserId(@PathVariable String userId) {
        return surveyService.getSurveyByUserId(userId);
    }
}