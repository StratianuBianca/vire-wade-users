package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "songs_spotify")
@Data
@Getter
@Setter
public class SongSpotify {
    @Id
    @Column(name = "song_spotify_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID songSpotify_id;
    private String url;
    private String name;
    private String artists;
    private UUID userId;
}
