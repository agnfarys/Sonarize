package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String clientId = "6bde7c93eba54dcb9b8bd1edec9d050b";
    private static final String clientSecret = "2f6849d460a84ce99dc0298d449994b5";
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
    public String callback(@RequestParam String code) {
        try {
            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
            AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());

            var userProfile = spotifyApi.getCurrentUsersProfile().build().execute();

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

            return "Authenticated and registered successfully!";
        } catch (Exception e) {
            return "Authentication failed: " + e.getMessage();
        }
    }

}