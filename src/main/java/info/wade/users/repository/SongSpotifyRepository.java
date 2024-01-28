package info.wade.users.repository;

import info.wade.users.entity.SongSpotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SongSpotifyRepository extends JpaRepository<SongSpotify, UUID> {
}
