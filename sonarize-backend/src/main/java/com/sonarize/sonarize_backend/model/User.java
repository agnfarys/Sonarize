package com.sonarize.sonarize_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
public class User {
    @Id
    private String id; // MongoDB ID (unikalne)

    private String spotifyId;    // ID użytkownika w Spotify
    private String username;     // Login użytkownika
    private String createdAt;    // Data utworzenia konta
    private String accessToken;  // Token dostępu Spotify
    private String refreshToken; // Token odświeżania Spotify
}
