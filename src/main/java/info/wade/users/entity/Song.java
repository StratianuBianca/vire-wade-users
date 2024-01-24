package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "songs")
@Data
@Getter
@Setter
public class Song {

    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String Album;
    private String creator;
    private String genre;
    private Date date;
    private String vinylLabel;
    private String title;
}
