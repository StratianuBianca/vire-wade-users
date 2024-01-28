package info.wade.users.repository;

import info.wade.users.entity.SongSpotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SongSpotifyRepository extends JpaRepository<SongSpotify, UUID> {
    Optional<SongSpotify> findByArtistsAndNameAndUrlAndAndUserIdAndSpotifyUrl(String artists, String name, String url, UUID userId, String spotifyUrl);
    Optional<SongSpotify> findByUserIdAndArtistsAndName(UUID userId, String artists, String name);
    Optional<SongSpotify> findBySpotifyUrlAndUserId(String spotifyUrl, UUID userId);
}
