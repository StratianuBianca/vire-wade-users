package info.wade.users.controller;

import info.wade.users.dto.SongSpotifyDTO;
import info.wade.users.dto.SongsSpotifyDTO;
import info.wade.users.service.SongSpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SongSpotifyController {

    @Autowired
    SongSpotifyService songSpotifyService;

    @GetMapping("/songs/spotify/{userId}")
    public ResponseEntity<?> getSongSpotifyByUser(@PathVariable UUID userId){
        List<SongSpotifyDTO> songSpotifyDTOList = songSpotifyService.getAllSongsByUser(userId);
        return new ResponseEntity<>(songSpotifyDTOList, HttpStatus.OK);
    }

    @PostMapping("/songs/spotify")
    public ResponseEntity<?> postSongSpotify(@RequestBody SongSpotifyDTO songSpotify) {
        SongSpotifyDTO songSpotifyDTO = songSpotifyService.createSongSpotify(songSpotify);
        if(songSpotifyDTO.getSongSpotify_id() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(songSpotifyDTO, HttpStatus.OK);
    }

    @PostMapping("/songs/multiple/spotify")
    public ResponseEntity<?> postSongsSpotify(@RequestBody SongsSpotifyDTO songsSpotify) {
        List<SongSpotifyDTO> songsSpotifyDTO = songSpotifyService.createMultipleSongSpotify(songsSpotify);
        return new ResponseEntity<>(songsSpotifyDTO, HttpStatus.OK);
    }

}
