package info.wade.users.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArtistDTO {
    private UUID id;

    private String name;

    private List<UUID> albumIds;
}
