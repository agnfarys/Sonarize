package com.sonarize.sonarize_backend.service;

import com.sonarize.sonarize_backend.model.Playlist;
import com.sonarize.sonarize_backend.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        playlist.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> findTopByUserIdOrderByCreatedAtDesc(String userId){
        return playlistRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Playlist> getPlaylistsByUserId(String userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist saveGeneratedPlaylist(String userId, String playlistId, List<String> trackUris) {
        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setPlaylistLink("https://open.spotify.com/playlist/" + playlistId);
        playlist.setTrackUris(trackUris);
        playlist.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return playlistRepository.save(playlist);
    }

}
