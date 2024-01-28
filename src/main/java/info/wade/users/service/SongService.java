package info.wade.users.service;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.PlaylistsDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.entity.Playlist;
import info.wade.users.entity.Song;
import info.wade.users.entity.User;
import info.wade.users.repository.PlaylistRepository;
import info.wade.users.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;


    public List<SongDTO> getAllSongs(){
        List<Song> songs = songRepository.findAll();
        List<SongDTO> songDTOS = new ArrayList<>();
        for(Song song:songs){
            SongDTO songDTO = new SongDTO();
            songDTO.setId(song.getId());
            songDTO.setAlbum(song.getAlbum());
            songDTO.setCreator(song.getCreator());
            songDTO.setDate(song.getDate());
            songDTO.setGenre(song.getGenre());
            songDTO.setVinylLabel(song.getVinylLabel());
            songDTO.setTitle(song.getTitle());
            songDTO.setDiscogs(song.getDiscogs());
            songDTO.setDiscogs_image(song.getDiscogs_image());
            songDTOS.add(songDTO);
        }
        return songDTOS;
    }

    public boolean addSongToMultiplePlaylists(UUID songId, UUID userId, PlaylistsDTO playlistIds){
        Optional<Song> queryResult = songRepository.findById(songId);
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            for(UUID id:playlistIds.getPlaylistIds()){
                Optional<Playlist> queryPlaylist = playlistRepository.findById(id);
                if(queryPlaylist.isPresent()){
                    Playlist playlist = queryPlaylist.get();
                    boolean isUserOk = this.isUserInUsersList(playlist.getUsers(), userId);
                    if(isUserOk){
                        playlist.addSong(song);
                        playlistRepository.save(playlist);
                    }
                    else{
                        return false;
                    }
                }
            }
            return true;
        }

        return false;
    }
    public boolean isUserInUsersList(List<User> users, UUID userId){

        for(User user:users){
            if(Objects.equals(user.getId(), userId)){
                return true;
            }
        }
        return false;
    }

    public SongDTO getSongById(UUID songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        SongDTO songDTO = new SongDTO();
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            songDTO.setId(song.getId());
            songDTO.setAlbum(song.getAlbum());
            songDTO.setCreator(song.getCreator());
            songDTO.setDate(song.getDate());
            songDTO.setGenre(song.getGenre());
            songDTO.setVinylLabel(song.getVinylLabel());
            songDTO.setTitle(song.getTitle());
            songDTO.setDiscogs_image(song.getDiscogs_image());
            songDTO.setDiscogs(song.getDiscogs());
            songDTO.setId(song.getId());

        }
        return songDTO;
    }

    public boolean deleteSong(UUID songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            songRepository.delete(song);
            return true;
        }
        return false;
    }

    public SongDTO createSong(SongDTO songDTO){
        Song song = new Song();
        song.setAlbum(songDTO.getAlbum());
        song.setCreator(songDTO.getCreator());
        song.setDate(songDTO.getDate());
        song.setGenre(songDTO.getGenre());
        song.setVinylLabel(songDTO.getVinylLabel());
        song.setTitle(songDTO.getTitle());
        song.setDiscogs(songDTO.getDiscogs());
        song.setDiscogs_image(songDTO.getDiscogs_image());
        songRepository.save(song);
        songDTO.setId(song.getId());
        return songDTO;
    }
    public SongDTO updateSong(SongDTO songDTO){
        Optional<Song> queryResult = songRepository.findById(songDTO.getId());
        if(queryResult.isPresent()){
            Song song = queryResult.get();
            song.setAlbum(songDTO.getAlbum());
            song.setCreator(songDTO.getCreator());
            song.setDate(songDTO.getDate());
            song.setGenre(songDTO.getGenre());
            song.setVinylLabel(songDTO.getVinylLabel());
            song.setTitle(songDTO.getTitle());
            song.setDiscogs_image(songDTO.getDiscogs_image());
            song.setDiscogs(songDTO.getDiscogs());

            songRepository.save(song);
            songDTO.setId(song.getId());
            return songDTO;
        }
        return new SongDTO();
    }
    public List<PlaylistDTO> getAllPlaylistWithSong(UUID songId){
        Optional<Song> queryResult = songRepository.findById(songId);
        if(queryResult.isPresent()){
            List<PlaylistDTO> playlistDTOS = new ArrayList<>();
            List<Playlist> playlists = playlistRepository.findAll();
            for(Playlist playlist:playlists){
                if(playlist.getSongs().contains(queryResult.get())){
                    PlaylistDTO playlistDTO = new PlaylistDTO();
                    playlistDTO.setCreatedDate(playlist.getCreate_date());
                    playlistDTO.setTitle(playlist.getTitle());
                    playlistDTO.setId(playlist.getPlaylist_id());
                    List<User> users = playlist.getUsers();
                    List<UUID> userIds = new ArrayList<>();
                    for(User user:users){
                        userIds.add(user.getId());
                    }
                    playlistDTO.setUserIds(userIds);
                    List<Song> songs = playlist.getSongs();
                    List<UUID> ids = new ArrayList<>();
                    for(Song song:songs){
                        ids.add(song.getId());
                    }
                    playlistDTO.setSongIds(ids);
                    playlistDTOS.add(playlistDTO);
                }
            }
            return playlistDTOS;
        }
        return new ArrayList<>();
    }

}
