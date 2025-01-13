package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String clientId = "6bde7c93eba54dcb9b8bd1edec9d050b";
    private static final String clientSecret = "8ab4cd78ad5740a08b8979b766187677";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/auth/callback");

    @Autowired
    private UserService userService;

    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    @GetMapping("/login")
    public String login() {
        String scopes = "user-read-recently-played playlist-modify-public playlist-modify-private user-top-read user-read-private user-library-read playlist-read-private";
        return "https://accounts.spotify.com/authorize?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&scope=" + scopes;
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws IOException {
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

            // Zakodowanie parametrów w URL
            String encodedUsername = URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8);
            String redirectUrl = "http://localhost:5173/survey?userId=" + user.getId()
                    + "&accessToken=" + credentials.getAccessToken()
                    + "&username=" + encodedUsername;

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            String encodedErrorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect("http://localhost:5173/error?message=" + encodedErrorMessage);
        }
    }
}
