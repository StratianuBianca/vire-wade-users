package info.wade.users.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ArtistSpotifyDTO {
    private UUID artistSpotify_id;
    private String url;
    private String urlSpotify;
    private String genres;
    private UUID userId;
}
