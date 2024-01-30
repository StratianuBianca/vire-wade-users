package info.wade.users.controller;

import info.wade.users.dto.ArtistSpotifyDTO;
import info.wade.users.dto.ArtistsSpotifyDTO;
import info.wade.users.service.ArtistSpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ArtistSpotifyController {

    @Autowired
    ArtistSpotifyService artistSpotifyService;  ///aiciiii

    @GetMapping("/artists/spotify/{userId}")
    public ResponseEntity<?> getArtistSpotifyByUser(@PathVariable UUID userId){
        List<ArtistSpotifyDTO> artistSpotifyDTOList = artistSpotifyService.getAllArtistByUser(userId);
        return new ResponseEntity<>(artistSpotifyDTOList, HttpStatus.OK);
    }

    @PostMapping("/artists/spotify")
    public ResponseEntity<?> postArtistSpotify(@RequestBody ArtistSpotifyDTO artistSpotify) {
        ArtistSpotifyDTO artistSpotifyDTO = artistSpotifyService.createArtistSpotify(artistSpotify);
        if(artistSpotify.getArtistSpotify_id() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artistSpotifyDTO, HttpStatus.OK);
    }

    @PostMapping("/artists/multiple/spotify")
    public ResponseEntity<?> postArtistsSpotify(@RequestBody ArtistsSpotifyDTO artistsSpotifyDTO) {
        List<ArtistSpotifyDTO> artistSpotifyDTOS = artistSpotifyService.createMultipleArtistSpotify(artistsSpotifyDTO);
        return new ResponseEntity<>(artistSpotifyDTOS, HttpStatus.OK);
    }
}
