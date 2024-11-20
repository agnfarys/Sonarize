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

}
