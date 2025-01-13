package com.sonarize.sonarize_backend.repository;

import com.sonarize.sonarize_backend.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    List<Playlist> findByUserId(String userId);

    Optional<Playlist> findTopByUserIdOrderByCreatedAtDesc(String userId);
}
