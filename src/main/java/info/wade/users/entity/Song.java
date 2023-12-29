package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;
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

    private String title;

    private String description;

    private float length;

    private Date release_date;

    private String category;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    public void deleteAlbum(){
        this.album = new Album();
    }

}
