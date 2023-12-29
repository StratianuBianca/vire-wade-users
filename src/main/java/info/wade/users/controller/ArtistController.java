package info.wade.users.controller;

import info.wade.users.dto.ArtistDTO;
import info.wade.users.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/artists") //ok
    public ResponseEntity<?> getAllArtists(){
        List<ArtistDTO> artistList = artistService.getAllArtists();
        return new ResponseEntity<>(artistList, HttpStatus.OK);
    }
    @GetMapping("/artists/{artistId}") //ok
    public ResponseEntity<?> getArtistById(@PathVariable UUID artistId){
        ArtistDTO artist = artistService.getArtistById(artistId);
        return new ResponseEntity<>(artist, HttpStatus.OK);

    }
    @PostMapping("/artists") //ok
    public ResponseEntity<?> postArtist(@RequestBody ArtistDTO artistDTO){
        ArtistDTO artist = artistService.addArtist(artistDTO);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }
    @PutMapping("/artists/{artistId}") //ok
    public ResponseEntity<?> putArtist(@PathVariable UUID artistId, @RequestBody ArtistDTO artistDTO){
        artistDTO.setId(artistId);
        ArtistDTO artist = artistService.updateArtist(artistDTO);
        if(artist.getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @DeleteMapping("/artists/{artistId}") //ok
    public ResponseEntity<?> deleteArtistById(@PathVariable UUID artistId){
        boolean exist = artistService.deleteArtist(artistId);
        if(exist){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
