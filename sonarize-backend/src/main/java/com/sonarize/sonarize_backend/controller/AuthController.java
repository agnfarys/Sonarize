package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String clientId = "6bde7c93eba54dcb9b8bd1edec9d050b";
    private static final String clientSecret = "8ab4cd78ad5740a08b8979b766187677";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/auth/callback");
    private static String code = "";

    @Autowired
    private UserService userService;

    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    private static final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
            .build();

    @GetMapping("/login")
    public String login() {
        String scopes = "user-read-recently-played playlist-modify-public playlist-modify-private";
        return "https://accounts.spotify.com/authorize?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&scope=" + scopes;
    }

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> callback(@RequestParam String code) {
        Map<String, String> response = new HashMap<>();

        try {
            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
            AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());

            var userProfile = spotifyApi.getCurrentUsersProfile().build().execute();

            if (userProfile == null || userProfile.getId() == null) {
                throw new IllegalArgumentException("Spotify user profile or ID is null.");
            }

            User user = userService.getUserBySpotifyId(userProfile.getId());
            if (user == null) {
                user = new User();
                user.setId(java.util.UUID.randomUUID().toString());
                user.setSpotifyId(userProfile.getId());
                user.setUsername(userProfile.getDisplayName());
                user.setCreatedAt(LocalDateTime.now().toString());
            }

            user.setAccessToken(credentials.getAccessToken());
            user.setRefreshToken(credentials.getRefreshToken());
            userService.saveUser(user);

            response.put("redirectUrl", "http://localhost:5173/survey");
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
