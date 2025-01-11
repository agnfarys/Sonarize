package com.sonarize.sonarize_backend.repository;

import com.sonarize.sonarize_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findBySpotifyId(String spotifyId);

}
