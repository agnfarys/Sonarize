package com.sonarize.sonarize_backend.repository;

import com.sonarize.sonarize_backend.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    List<Playlist> findByUserId(String userId);
}
