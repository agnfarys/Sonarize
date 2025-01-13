package com.sonarize.sonarize_backend.util;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;

import java.util.List;

public class TestDataUtil {

    public static User createTestUser() {
        User user = new User();
        user.setId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
        user.setSpotifyId("461b35iwd06kspq5tc42o47dr");
        user.setUsername("Wojciech");
        user.setCreatedAt("2024-11-20T15:50:40.805931");
        return user;
    }

    public static Survey createTestSurvey() {
        Survey survey = new Survey();
        survey.setId("675e1700036fa2598c708d6c");
        survey.setUserId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
        survey.setMood("happy");
        survey.setGenres(List.of("pop"));
        survey.setEnergyLevel("moderate");
        survey.setOccasion("workout");
        survey.setFavoriteArtists(List.of("Imagine Dragons"));
        survey.setDiscoveryPreference("new releases");
        survey.setLanguagePreference("english");
        survey.setPlaylistLength("10");
        survey.setCreatedAt("2024-12-15T00:38:40.5150181");
        return survey;
    }
}
