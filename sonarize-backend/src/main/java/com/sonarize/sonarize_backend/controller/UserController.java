package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.ChatGPTService;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sonarize.sonarize_backend.controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/find")
    public User findUserByUsername(@RequestParam String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping("getCurrentUserProfile")
    public se.michaelthelin.spotify.model_objects.specification.User[] getCurrentUserProfile(){
        final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();

        try {
            return new se.michaelthelin.spotify.model_objects.specification.User[]{getCurrentUsersProfileRequest.execute()};
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new se.michaelthelin.spotify.model_objects.specification.User[0];
    }

    @GetMapping("/recently-played")
    public Object getRecentlyPlayedTracks() {
        try {
            var response = spotifyApi.getCurrentUsersRecentlyPlayedTracks().limit(10).build().execute();
            return Arrays.stream(response.getItems())
                    .map(item -> Map.of(
                            "track", item.getTrack().getName(),
                            "artist", Arrays.stream(item.getTrack().getArtists())
                                    .map(ArtistSimplified::getName)
                                    .collect(Collectors.joining(", "))
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching recently played tracks: " + e.getMessage());
        }
    }
}

