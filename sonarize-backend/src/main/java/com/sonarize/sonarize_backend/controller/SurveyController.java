package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Survey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{userId}/")
    public ResponseEntity<?> getUserSurveys(@PathVariable String userId) {
        try {
            List<Survey> surveys = getSurveysByUserId(userId);
            if (surveys.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No surveys found for the user.");
            }

            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving surveys: " + e.getMessage());
        }
    }
}