package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Playlist;
import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.ChatGPTService;
import com.sonarize.sonarize_backend.service.PlaylistService;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sonarize.sonarize_backend.controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private UserService userService;

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
    public String generateChatPlaylist(@RequestBody Survey survey, @RequestParam String userId) {
        try {
            // Pobieranie użytkownika
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + userId);
            }

            spotifyApi.setAccessToken(user.get().getAccessToken());

            // Generowanie rekomendacji
            List<String> recommendedTracks = chatGPTService.generateRecommendations(survey);
            if (recommendedTracks == null || recommendedTracks.isEmpty()) {
                throw new RuntimeException("No recommended tracks generated.");
            }

            recommendedTracks = recommendedTracks.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (recommendedTracks.isEmpty()) {
                throw new RuntimeException("All recommended tracks are null.");
            }

            // Tworzenie playlisty
            var createPlaylistRequest = spotifyApi.createPlaylist(user.get().getSpotifyId(), "ChatGPT Generated Playlist")
                    .description("Playlist created based on ChatGPT recommendations")
                    .public_(false)
                    .build();
            var playlist = createPlaylistRequest.execute();

            // Wyszukiwanie i dodawanie utworów
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

            return "Playlist created successfully: https://open.spotify.com/playlist/" + playlist.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error generating playlist: " + e.getMessage());
        }
    }

}
