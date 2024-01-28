package info.wade.users.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SongSpotifyDTO {
    private UUID songSpotify_id;
    private String url;
    private String name;
    private String artists;
    private String spotifyUrl;
    private UUID userId;
}
