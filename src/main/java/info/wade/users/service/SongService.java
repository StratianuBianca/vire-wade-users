package info.wade.users.service;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.PlaylistsDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.entity.Album;
import info.wade.users.entity.Playlist;
import info.wade.users.entity.Song;
import info.wade.users.entity.User;
import info.wade.users.repository.AlbumRepository;
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
    private AlbumRepository albumRepository;

    @Autowired
    private PlaylistRepository playlistRepository;


    public List<SongDTO> getAllSongs(){
        List<Song> songs = songRepository.findAll();
        List<SongDTO> songDTOS = new ArrayList<>();
        for(Song song:songs){
            SongDTO songDTO = new SongDTO();
            songDTO.setDescription(song.getDescription());
            songDTO.setLength(song.getLength());
            songDTO.setTitle(song.getTitle());
            songDTO.setRelease_date(song.getRelease_date());
            songDTO.setId(song.getId());
            songDTO.setAlbumId(song.getAlbum().getId());
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
            songDTO.setDescription(song.getDescription());
            songDTO.setLength(song.getLength());
            songDTO.setTitle(song.getTitle());
            songDTO.setRelease_date(song.getRelease_date());
            songDTO.setAlbumId(song.getAlbum().getId());
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
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        Song song = new Song();
        if(album.isPresent()){
            song.setAlbum(album.get());
            song.setLength(songDTO.getLength());
            song.setTitle(songDTO.getTitle());
            song.setDescription(songDTO.getDescription());
            song.setRelease_date(songDTO.getRelease_date());
            songRepository.save(song);
            Album album1 = album.get();
            album1.addToSongs(song);
            albumRepository.save(album1);
            songDTO.setId(song.getId());
        }
        return songDTO;
    }
    public SongDTO updateSong(SongDTO songDTO){
        Optional<Song> queryResult = songRepository.findById(songDTO.getId());
        Optional<Album> album = albumRepository.findById(songDTO.getAlbumId());
        if(queryResult.isPresent() && album.isPresent()){
            Song song = queryResult.get();
            song.setAlbum(album.get());
            song.setLength(songDTO.getLength());
            song.setTitle(songDTO.getTitle());
            song.setDescription(songDTO.getDescription());
            song.setRelease_date(songDTO.getRelease_date());
            songRepository.save(song);
            Album album1 = album.get();
            album1.addToSongs(song);
            albumRepository.save(album1);
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
