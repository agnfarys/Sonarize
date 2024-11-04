package com.sonarize.sonarize_backend.repository;

import com.sonarize.sonarize_backend.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, String> {
    List<Survey> findByUserId(String userId);
}
