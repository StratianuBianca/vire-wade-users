package info.wade.users.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artist")
@Data
@Getter
@Setter
public class Artist {

    @Id
    @Column(name = "artist_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "artists")
    private List<Album> albums;


}
