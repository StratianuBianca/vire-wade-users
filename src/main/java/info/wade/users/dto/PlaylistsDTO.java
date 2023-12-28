package info.wade.users.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistsDTO {
    private List<Long> playlistIds;
}
