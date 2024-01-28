package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Entity
@Table(name = "artists_spotify")
@Data
@Getter
@Setter
public class ArtistSpotify {
    @Id
    @Column(name = "artist_spotify_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID artistSpotify_id;
    private String url;
    private String urlSpotify;
    private String genres;
    private UUID userId;

}
