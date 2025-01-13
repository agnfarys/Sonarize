package com.sonarize.sonarize_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonarize.sonarize_backend.model.Playlist;
import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.ChatGPTService;
import com.sonarize.sonarize_backend.service.PlaylistService;
import com.sonarize.sonarize_backend.service.SurveyService;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.sonarize.sonarize_backend.controller.AuthController.spotifyApi;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private UserService userService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/create")
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        return playlistService.createPlaylist(playlist);
    }

    @GetMapping("/user/{userId}")
    public List<Playlist> getPlaylistsByUserId(@PathVariable String userId) {
        return playlistService.getPlaylistsByUserId(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserPlaylists(@PathVariable String userId) {
        try {
            List<Playlist> playlists = playlistService.getPlaylistsByUserId(userId);
            if (playlists.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No playlists found for the user.");
            }
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving playlists: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/last")
    public ResponseEntity<?> getLastPlaylist(@PathVariable String userId) {
        try {
            Optional<Playlist> lastPlaylist = playlistService.findTopByUserIdOrderByCreatedAtDesc(userId);

            if (lastPlaylist.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No playlist found for this user.");
            }

            return ResponseEntity.ok(lastPlaylist.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving last playlist: " + e.getMessage());
        }
    }

    @PostMapping("/create-from-songs")
    public String createPlaylistFromSongs(@RequestBody List<Map<String, String>> songs, @RequestParam String userId) {
        try {
            var createPlaylistRequest = spotifyApi.createPlaylist(userId, "Generated Playlist")
                    .description("Playlist created from provided songs")
                    .public_(false)
                    .build();
            var playlist = createPlaylistRequest.execute();

            List<String> uris = songs.stream()
                    .map(song -> {
                        try {
                            var search = spotifyApi.searchTracks(song.get("title") + " " + song.get("artist"))
                                    .limit(1)
                                    .build()
                                    .execute();
                            return search.getItems()[0].getUri();
                        } catch (Exception e) {
                            throw new RuntimeException("Error finding track: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());
            spotifyApi.addItemsToPlaylist(playlist.getId(), uris.toArray(new String[0])).build().execute();

            return "Playlist created successfully: https://open.spotify.com/playlist/" + playlist.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error creating playlist: " + e.getMessage());
        }
    }

    @PostMapping("/generate-chat-playlist")
    public ResponseEntity<Map<String, String>> generateChatPlaylist(@RequestBody Survey survey, @RequestParam String userId) {
        Map<String, String> response = new HashMap<>();

        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + userId);
            }

            spotifyApi.setAccessToken(user.get().getAccessToken());

            List<String> recommendedTracks = chatGPTService.generateRecommendations(survey);
            if (recommendedTracks == null || recommendedTracks.isEmpty()) {
                throw new RuntimeException("No recommended tracks generated.");
            }

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String playlistTitle = "ChatGPT Playlist - " + currentDateTime;

            ObjectMapper objectMapper = new ObjectMapper();
            String surveyDescription = objectMapper.writeValueAsString(survey);

            var createPlaylistRequest = spotifyApi.createPlaylist(user.get().getSpotifyId(), playlistTitle)
                    .description("Survey details: " + surveyDescription)
                    .public_(false)
                    .build();
            var playlist = createPlaylistRequest.execute();

            List<String> uris = recommendedTracks.stream()
                    .map(track -> {
                        try {
                            var search = spotifyApi.searchTracks(track).limit(1).build().execute();
                            if (search == null || search.getItems().length == 0) {
                                throw new RuntimeException("Track not found: " + track);
                            }
                            return search.getItems()[0].getUri();
                        } catch (Exception e) {
                            throw new RuntimeException("Error finding track: " + track + " - " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());

            if (uris.isEmpty()) {
                throw new RuntimeException("No valid tracks found to add to playlist.");
            }

            spotifyApi.addItemsToPlaylist(playlist.getId(), uris.toArray(new String[0])).build().execute();

            playlistService.saveGeneratedPlaylist(userId, playlist.getId(), uris);
            survey.setUserId(userId);
            surveyService.createSurvey(survey);

            response.put("message", "Playlist created successfully");
            response.put("playlistUrl", "https://open.spotify.com/playlist/" + playlist.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Obsługujemy wyjątek i zwracamy odpowiedni komunikat błędu w JSON
            response.put("error", "Error generating playlist: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserSummary(@PathVariable String userId) {
        try {
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            spotifyApi.setAccessToken(user.get().getAccessToken());

            var topArtistsRequest = spotifyApi.getUsersTopArtists()
                    .limit(50)
                    .time_range("medium_term")
                    .build();

            var topArtists = topArtistsRequest.execute().getItems();

            List<String> topArtistsList = Arrays.stream(topArtists)
                    .limit(4)
                    .map(artist -> artist.getName())
                    .collect(Collectors.toList());

            Map<String, Long> genreCount = Arrays.stream(topArtists)
                    .map(artist -> artist.getGenres())
                    .flatMap(Arrays::stream)
                    .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));

            List<String> topGenres = genreCount.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .map(Map.Entry::getKey)
                    .limit(4)
                    .collect(Collectors.toList());

            Map<String, Object> summary = Map.of(
                    "topArtists", topArtistsList,
                    "topGenres", topGenres
            );

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            System.err.println("Error fetching user summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching user summary: " + e.getMessage()));
        }
    }
}
