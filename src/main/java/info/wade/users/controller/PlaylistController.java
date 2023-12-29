package info.wade.users.controller;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/playlists") //ok
    public ResponseEntity<?> getAllPlaylists(){
        List<PlaylistDTO> playlistsList = playlistService.getAll();
        return new ResponseEntity<>(playlistsList, HttpStatus.OK);
    }
    @GetMapping("/playlists/{playlistId}") //ok
    public ResponseEntity<?> getPlaylistById(@PathVariable Long playlistId){
        PlaylistDTO playlist = playlistService.getPlaylistById(playlistId);
        return new ResponseEntity<>(playlist, HttpStatus.OK);

    }
    @GetMapping("/playlists/user/{userId}") //
    public ResponseEntity<?> getPlaylistsByUserId(@PathVariable Long userId){
        List<PlaylistDTO> playlist = playlistService.getAllPlaylistsFromAUser(userId);
        return new ResponseEntity<>(playlist, HttpStatus.OK);

    }

    @GetMapping("/playlists/songs/{playlistId}") //ok
    public ResponseEntity<?> getAllSongOfPlaylist(@PathVariable Long playlistId){
        List<SongDTO> songs = playlistService.getAllSongsByPlaylist(playlistId);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/playlists/verify/user/{userId}/playlist/{playlistId}")
    public ResponseEntity<?> verifyIfUserIsOk(@PathVariable Long userId, @PathVariable Long playlistId){
        boolean isOk = playlistService.verifyIfUserHasPlaylist(userId, playlistId);
        if(isOk){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/playlists/songs/{playlistId}/{userId}") //ok
    public ResponseEntity<?> getAllSongOfPlaylistByUserId(@PathVariable Long playlistId, @PathVariable Long userId){
        List<SongDTO> songs = playlistService.getAllSongsByPlaylistUserId(playlistId, userId);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping("/playlists") //ok
    public ResponseEntity<?> postPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO playlist = playlistService.createPlaylist(playlistDTO);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }
    @PutMapping("/playlists/{playlistId}/songs/{songId}/user/{userId}") //ok
    public ResponseEntity<?> postSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId, @PathVariable Long userId){
        playlistService.addSongToPlaylist(playlistId, songId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/playlist/{playlistId}/songs/{songId}/user/{userId}") //ok
    public ResponseEntity<?> deleteSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId, @PathVariable Long userId){
        playlistService.deleteSongFromPlaylist(playlistId, songId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/playlists/{playlistId}/{userId}") //ok
    public ResponseEntity<?> putPlaylist(@PathVariable Long playlistId, @PathVariable Long userId, @RequestBody PlaylistDTO playlistDTO) {
        playlistDTO.setId(playlistId);
        PlaylistDTO playlist = playlistService.updatePlaylist(playlistDTO, userId);
        if(playlist.getId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @PutMapping("/playlists/users/{playlistId}/{userId}")
    public ResponseEntity<?> addUserToPlaylist(@PathVariable Long playlistId, @PathVariable Long userId){
        boolean ok = playlistService.addUserToPlaylist(playlistId, userId);
        if(ok){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @DeleteMapping("/playlists/{playlistId}/{userId}") //ok
    public ResponseEntity<?> deletePlaylistById(@PathVariable Long playlistId, @PathVariable Long userId){
        boolean exist = playlistService.deletePlaylist(playlistId, userId);
        if(exist){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
