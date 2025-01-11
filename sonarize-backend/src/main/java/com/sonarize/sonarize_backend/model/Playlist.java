package com.sonarize.sonarize_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "playlists")
@Getter
@Setter
public class Playlist {
    @Id
    private String id;
    private String userId;
    private List<String> trackUris;
    private String playlistLink;
    private String createdAt;

}
