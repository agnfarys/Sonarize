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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTrackUris() {
        return trackUris;
    }

    public void setTrackUris(List<String> trackUris) {
        this.trackUris = trackUris;
    }

    public String getPlaylistLink() {
        return playlistLink;
    }

    public void setPlaylistLink(String playlistLink) {
        this.playlistLink = playlistLink;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
