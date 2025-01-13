package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Playlist;
import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.repository.PlaylistRepository;
import com.sonarize.sonarize_backend.repository.SurveyRepository;
import com.sonarize.sonarize_backend.repository.UserRepository;
import com.sonarize.sonarize_backend.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.sonarize.sonarize_backend.controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRecommendations(@PathVariable String userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            User user = userOptional.get();

            // Ustaw token Spotify dla zalogowanego użytkownika
            spotifyApi.setAccessToken(user.getAccessToken());

            // Pobierz preferencje użytkownika
            List<Survey> userSurveys = surveyRepository.findByUserId(userId);
            Set<String> userGenres = userSurveys.stream()
                    .flatMap(survey -> survey.getGenres().stream())
                    .collect(Collectors.toSet());
            Set<String> userArtists = userSurveys.stream()
                    .flatMap(survey -> survey.getFavoriteArtists().stream())
                    .collect(Collectors.toSet());

            // Pobierz innych użytkowników
            List<User> allUsers = userRepository.findAll();
            List<User> otherUsers = allUsers.stream()
                    .filter(u -> !u.getId().equals(userId))
                    .collect(Collectors.toList());

            // Oblicz podobieństwo
            Map<User, Double> userSimilarityScores = new HashMap<>();
            for (User otherUser : otherUsers) {
                List<Survey> otherUserSurveys = surveyRepository.findByUserId(otherUser.getId());
                Set<String> otherUserGenres = otherUserSurveys.stream()
                        .flatMap(survey -> survey.getGenres().stream())
                        .collect(Collectors.toSet());
                Set<String> otherUserArtists = otherUserSurveys.stream()
                        .flatMap(survey -> survey.getFavoriteArtists().stream())
                        .collect(Collectors.toSet());

                long commonGenres = userGenres.stream().filter(otherUserGenres::contains).count();
                long commonArtists = userArtists.stream().filter(otherUserArtists::contains).count();

                double similarityScore = commonGenres * 0.7 + commonArtists * 0.3;
                userSimilarityScores.put(otherUser, similarityScore);
            }

            // Znajdź najbardziej podobnych użytkowników
            List<User> similarUsers = userSimilarityScores.entrySet().stream()
                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Pobierz playlisty podobnych użytkowników
            List<Playlist> similarPlaylists = new ArrayList<>();
            for (User similarUser : similarUsers) {
                List<Playlist> playlists = playlistRepository.findByUserId(similarUser.getId());
                similarPlaylists.addAll(playlists);
            }

            // Fallback: Domyślne playlisty
            if (similarPlaylists.isEmpty()) {
                similarPlaylists = playlistRepository.findAll().stream()
                        .limit(10)
                        .collect(Collectors.toList());
            }

            // Pobierz najpopularniejsze utwory ze Spotify
            List<String> popularTracks = new ArrayList<>();
            try {
                var featuredPlaylists = spotifyApi.getListOfFeaturedPlaylists()
                        .limit(5)
                        .build()
                        .execute();
                for (var playlist : featuredPlaylists.getPlaylists().getItems()) {
                    var tracks = spotifyApi.getPlaylistsItems(playlist.getId())
                            .limit(5)
                            .build()
                            .execute();
                    for (var track : tracks.getItems()) {
                        if (track.getTrack() != null) {
                            popularTracks.add(track.getTrack().getName());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching popular tracks: " + e.getMessage());
            }

            // Rekomendacje od ChatGPT
            List<String> chatGPTRecommendations = !userSurveys.isEmpty()
                    ? chatGPTService.generateRecommendations(userSurveys.get(0))
                    : chatGPTService.generateDefaultRecommendations();

            // Przygotuj odpowiedź
            Map<String, Object> response = new HashMap<>();
            response.put("recommendedPlaylists", similarPlaylists);
            response.put("popularTracks", popularTracks);
            response.put("chatGPTRecommendations", chatGPTRecommendations);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating recommendations: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/similar-last")
    public ResponseEntity<?> getSimilarUsersLastPlaylists(@PathVariable String userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            User user = userOptional.get();

            // Pobierz preferencje użytkownika
            List<Survey> userSurveys = surveyRepository.findByUserId(userId);
            Set<String> userGenres = userSurveys.stream()
                    .flatMap(survey -> survey.getGenres().stream())
                    .collect(Collectors.toSet());

            // Pobierz innych użytkowników
            List<User> allUsers = userRepository.findAll();
            List<User> similarUsers = allUsers.stream()
                    .filter(u -> !u.getId().equals(userId))
                    .limit(5)
                    .collect(Collectors.toList());

            // Pobierz ostatnie playlisty podobnych użytkowników
            List<Playlist> lastPlaylists = new ArrayList<>();
            for (User similarUser : similarUsers) {
                playlistRepository.findTopByUserIdOrderByCreatedAtDesc(similarUser.getId())
                        .ifPresent(lastPlaylists::add);
            }

            return ResponseEntity.ok(lastPlaylists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving similar users' playlists: " + e.getMessage());
        }
    }
}

