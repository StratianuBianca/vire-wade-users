package info.wade.users.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class PlaylistDTO {
    private UUID id;

    private String title;
    private String category;
    private List<UUID> songIds;
    private List<UUID> userIds;
    private Date createdDate;
}
