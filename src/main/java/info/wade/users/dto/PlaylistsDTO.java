package info.wade.users.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PlaylistsDTO {
    private List<UUID> playlistIds;
}
