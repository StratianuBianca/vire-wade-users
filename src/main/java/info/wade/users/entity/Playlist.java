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
@Table(name = "playlists")
@Data
@Getter
@Setter
public class Playlist {
    @Id
    @Column(name = "playlist_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID playlist_id;

    private String title;

    @ManyToMany(
            fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "playlist_user",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    private Date create_date;

    public void addSong(Song song){
        this.songs.add(song);
    }

    public void addUser(User user){
        this.users.add(user);
    }
}
