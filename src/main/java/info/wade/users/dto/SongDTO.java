package info.wade.users.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class SongDTO {

    private UUID id;

    private String creator;
    private String genre;

    private Date date;
    private String vinylLabel;
    private String discogs;
    private String discogs_image;

}
