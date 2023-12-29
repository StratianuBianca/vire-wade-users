package info.wade.users.dto;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AlbumDTO {
    private UUID id;
    private String title;
    private String description;
    private List<UUID> artistIds;
    private List<UUID> songIds;
}
