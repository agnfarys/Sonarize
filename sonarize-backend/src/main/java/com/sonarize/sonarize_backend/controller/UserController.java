package com.sonarize.sonarize_backend.controller;

import com.sonarize.sonarize_backend.model.Survey;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.ChatGPTService;
import com.sonarize.sonarize_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping("/test")
    public List<String> test() {
        Survey survey = new Survey();
        survey.setMood("HAPPY");
        survey.setGenres(List.of("ROCK", "POP"));
        survey.setEnergyLevel("80");
        survey.setOccasion("PARTY");
        survey.setFavoriteArtists(List.of("Roling Stones", "AC/DC"));
        survey.setDiscoveryPreference("NEW_RELEASES");
        survey.setLanguagePreference("EN");
        survey.setPlaylistLength("5");

        List<String> recommendations = chatGPTService.generateRecommendations(survey);
        System.out.println("Recommended Playlist: " + recommendations);
        return recommendations;
    }

}