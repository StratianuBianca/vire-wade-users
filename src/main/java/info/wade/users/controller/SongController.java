package info.wade.users.controller;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.PlaylistsDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("/songs") //ok
    public ResponseEntity<?> getAllSongs(){
        List<SongDTO> songList = songService.getAllSongs();
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }
    @GetMapping("/songs/{songId}") //ok
    public ResponseEntity<?> getSongById(@PathVariable UUID songId){
        SongDTO song = songService.getSongById(songId);
        return new ResponseEntity<>(song, HttpStatus.OK);

    }
    @GetMapping("songs/pageable")
    public ResponseEntity<?> getAllSongsPag(@RequestParam(name = "page",defaultValue = "0") Integer page, @RequestParam(name="size", defaultValue = "12") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        List<SongDTO> songDTOS = songService.getAllSongsPageable(pageable);
        return new ResponseEntity<>(songDTOS, HttpStatus.OK);

    }
    @PostMapping("/songs") //ok
    public ResponseEntity<?> postSong(@RequestBody SongDTO songDTO) {
        SongDTO song = songService.createSong(songDTO);
        if(song.getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/songs/{songId}") //ok
    public ResponseEntity<?> putSong(@PathVariable UUID songId, @RequestBody SongDTO songDTO) {
        songDTO.setId(songId);
        SongDTO song = songService.updateSong(songDTO);
        if(song.getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(song, HttpStatus.OK);
    }
    @PutMapping("/songs/{songId}/playlists/user/{userId}")
    public ResponseEntity<?> postSongToMultiplePlaylists(@PathVariable UUID songId, @PathVariable UUID userId, @RequestBody PlaylistsDTO playlistIds){
        boolean exist = songService.addSongToMultiplePlaylists(songId, userId, playlistIds);
        if(exist){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/songs/playlists/{songId}") //ok
    public ResponseEntity<?> getAllPlaylistWithSong(@PathVariable UUID songId){
        List<PlaylistDTO> playlists = songService.getAllPlaylistWithSong(songId);
        if(playlists.size() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(playlists, HttpStatus.OK);

    }

    @DeleteMapping("/songs/{songId}") ///ok
    public ResponseEntity<?> deleteSongById(@PathVariable UUID songId){
        boolean exist = songService.deleteSong(songId);
        if(exist){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
