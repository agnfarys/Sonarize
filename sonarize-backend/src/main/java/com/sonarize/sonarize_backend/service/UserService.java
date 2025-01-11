package com.sonarize.sonarize_backend.service;


import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserBySpotifyId(String spotifyId) {
        return userRepository.findBySpotifyId(spotifyId);
    }

    public void saveUser(User user) {
        if (user.getId() == null || user.getSpotifyId() == null) {
            throw new IllegalArgumentException("User must have a valid id and Spotify ID.");
        }
        userRepository.save(user);
    }


    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

}
