package com.sonarize.sonarize_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "surveys")
@Setter
@Getter
public class Survey {
    @Id
    private String id;
    private String userId;
    private String mood;
    private List<String> genres;
    private String energyLevel;
    private String occasion;
    private List<String> favoriteArtists;
    private String discoveryPreference;
    private String languagePreference;
    private String playlistLength;
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

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public List<String> getFavoriteArtists() {
        return favoriteArtists;
    }

    public void setFavoriteArtists(List<String> favoriteArtists) {
        this.favoriteArtists = favoriteArtists;
    }

    public String getDiscoveryPreference() {
        return discoveryPreference;
    }

    public void setDiscoveryPreference(String discoveryPreference) {
        this.discoveryPreference = discoveryPreference;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getPlaylistLength() {
        return playlistLength;
    }

    public void setPlaylistLength(String playlistLength) {
        this.playlistLength = playlistLength;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
