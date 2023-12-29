package info.wade.users.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class SongDTO {

    private UUID id;

    private String title;

    private String description;

    private float length;

    private Date release_date;

    private UUID albumId;

}
