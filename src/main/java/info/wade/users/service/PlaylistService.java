package info.wade.users.service;

import info.wade.users.dto.PlaylistDTO;
import info.wade.users.dto.SongDTO;
import info.wade.users.entity.Playlist;
import info.wade.users.entity.Song;
import info.wade.users.entity.User;
import info.wade.users.repository.PlaylistRepository;
import info.wade.users.repository.SongRepository;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private List<PlaylistDTO> getAllPlaylistDTOS(List<Playlist> playlists){
        List<PlaylistDTO> playlistDTOS = new ArrayList<>();
        for(Playlist playlist:playlists){
            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.setTitle(playlist.getTitle());
            playlistDTO.setId(playlist.getPlaylist_id());
            List<User> users = playlist.getUsers();
            List<UUID> userIds = new ArrayList<>();
            for(User user:users){
                userIds.add(user.getId());
            }
            playlistDTO.setUserIds(userIds);
            playlistDTO.setCreatedDate(playlist.getCreate_date());
            List<UUID> ids = new ArrayList<>();
            List<Song> songs = playlist.getSongs();
            for(Song song:songs){
                ids.add(song.getId());
            }
            playlistDTO.setSongIds(ids);
            playlistDTOS.add(playlistDTO);
        }
        return playlistDTOS;
    }
    public List<PlaylistDTO> getAll(){
        List<Playlist> playlists = playlistRepository.findAll();
        return this.getAllPlaylistDTOS(playlists);
    }
    public PlaylistDTO getPlaylistById(UUID id){
        Optional<Playlist> queryResult = playlistRepository.findById(id);
        PlaylistDTO playlistDTO = new PlaylistDTO();
        if(queryResult.isPresent()){
            Playlist playlist = queryResult.get();
            playlistDTO.setTitle(playlist.getTitle());
            playlistDTO.setId(playlist.getPlaylist_id());
            List<User> users = playlist.getUsers();
            List<UUID> userIds = new ArrayList<>();
            for(User user:users){
                userIds.add(user.getId());
            }
            playlistDTO.setUserIds(userIds);
            playlistDTO.setCreatedDate(playlist.getCreate_date());
            List<Song> songs = playlist.getSongs();
            List<UUID> ids = new ArrayList<>();
            for(Song song:songs){
                ids.add(song.getId());
            }
            playlistDTO.setSongIds(ids);
        }
        return playlistDTO;
    }
    public boolean deletePlaylist(UUID playlistId, UUID userId){
        Optional<Playlist> queryResult = playlistRepository.findById(playlistId);
        if(queryResult.isPresent()){
            Playlist playlist = queryResult.get();
            boolean isUserOk = this.isUserInUsersList(playlist.getUsers(), userId);
            if(isUserOk){
                List<Song> songs = playlist.getSongs();
                songRepository.saveAll(songs);
                playlistRepository.delete(playlist);
                return true;
            }
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
    public boolean verifyIfUserHasPlaylist(UUID userId, UUID playlistId){
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(playlistOptional.isPresent() && userOptional.isPresent()){
            Playlist playlist = playlistOptional.get();
            return this.isUserInUsersList(playlist.getUsers(), userId);
        }
        return false;
    }
    public boolean addUserToPlaylist(UUID playlistId, UUID userId){
        Optional<Playlist> queryResultPlaylist = playlistRepository.findById(playlistId);
        Optional<User> queryResultUser = userRepository.findById(userId);
        if(queryResultPlaylist.isPresent() && queryResultUser.isPresent()){
            Playlist playlist = queryResultPlaylist.get();
            User user = queryResultUser.get();
            playlist.addUser(user);
            playlistRepository.save(playlist);
            return true;
        }
        return false;

    }
    public List<Playlist> userPlaylists(List<Playlist> playlists, UUID userId){
        List<Playlist> userPlaylists = new ArrayList<>();
        for(Playlist playlist:playlists){
            boolean userOk = this.isUserInUsersList(playlist.getUsers(), userId);
            if(userOk){
                userPlaylists.add(playlist);
            }
        }
        return userPlaylists;
    }

    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO){
        Playlist playlist = new Playlist();
        List<User> users = new ArrayList<>();
        boolean ok = true;
        for(UUID id:playlistDTO.getUserIds()){
            Optional<User> user = userRepository.findById(id);
            if(user.isEmpty()){
                ok = false;
            }

        }
        if(ok){
            for(UUID id:playlistDTO.getUserIds()){
                Optional<User> user = userRepository.findById(id);
                user.ifPresent(users::add);

            }
            playlist.setUsers(users);
            playlist.setTitle(playlistDTO.getTitle());
            System.out.println(LocalDate.now());
            playlist.setCreate_date(Date.from(LocalDateTime.now().atZone(ZoneId.of("Europe/Bucharest")).toInstant()));
            List<Song> songs = this.setSongs(playlistDTO.getSongIds());
            playlist.setSongs(songs);
            playlistRepository.save(playlist);
            playlistDTO.setId(playlist.getPlaylist_id());
            playlistDTO.setCreatedDate(playlist.getCreate_date());
        }
        return playlistDTO;
    }

    public PlaylistDTO updatePlaylist(PlaylistDTO playlistDTO, UUID userId){
        Optional<Playlist> queryResult = playlistRepository.findById(playlistDTO.getId());
        boolean ok = true;
        List<User> users = new ArrayList<>();
        for(UUID id:playlistDTO.getUserIds()){
            Optional<User> user = userRepository.findById(id);
            if(user.isEmpty()){
                ok = false;
            }

        }
        if(queryResult.isPresent() && ok){
            Playlist playlist = queryResult.get();
            boolean isUserOk = this.isUserInUsersList(playlist.getUsers(), userId);
            if(isUserOk){
                playlist.setTitle(playlistDTO.getTitle());
                for(UUID id:playlistDTO.getUserIds()){
                    Optional<User> user = userRepository.findById(id);
                    user.ifPresent(users::add);

                }
                playlist.setUsers(users);
                playlist.setCreate_date(playlistDTO.getCreatedDate());
                playlist.setSongs(this.setSongs(playlistDTO.getSongIds()));
                playlistRepository.save(playlist);
                playlistDTO.setId(playlist.getPlaylist_id());
                return playlistDTO;
            }

        }
        return new PlaylistDTO();
    }

    public List<PlaylistDTO> getAllPlaylistsFromAUser(UUID userId){
        Optional<User> user = userRepository.findById(userId);
        List<Playlist> playlists = playlistRepository.findAll();
        if(user.isPresent()){
            playlists = this.userPlaylists(playlists, userId);
        }

        return this.getAllPlaylistDTOS(playlists);
    }

    public List<SongDTO> getAllSongsByPlaylist(UUID playlistId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        List<SongDTO> songDTOS = new ArrayList<>();
        if(playlist.isPresent()){
            for(Song song:playlist.get().getSongs()){
                SongDTO songDTO = new SongDTO();
                songDTO.setId(song.getId());
                songDTO.setLength(song.getLength());
                songDTO.setDescription(song.getDescription());
                songDTO.setRelease_date(song.getRelease_date());
                songDTO.setTitle(song.getTitle());
                songDTO.setAlbumId(song.getAlbum().getId());
                songDTOS.add(songDTO);
            };
        }
        return songDTOS;
    }
    public List<SongDTO> getAllSongsByPlaylistUserId(UUID playlistId, UUID userId){
        Optional<Playlist> queryResult = playlistRepository.findById(playlistId);
        List<SongDTO> songDTOS = new ArrayList<>();
        if(queryResult.isPresent()){
            Playlist playlist = queryResult.get();
            boolean userOk = this.isUserInUsersList(playlist.getUsers(), userId);
            if(userOk){
                for(Song song:playlist.getSongs()){
                    SongDTO songDTO = new SongDTO();
                    songDTO.setId(song.getId());
                    songDTO.setLength(song.getLength());
                    songDTO.setDescription(song.getDescription());
                    songDTO.setRelease_date(song.getRelease_date());
                    songDTO.setTitle(song.getTitle());
                    songDTO.setAlbumId(song.getAlbum().getId());
                    songDTOS.add(songDTO);
                }
            }
        }
        return songDTOS;
    }

    public void addSongToPlaylist(UUID playlistId, UUID songId, UUID userId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Song> song = songRepository.findById(songId);
        if(playlist.isPresent() && song.isPresent()){
            Playlist playlist1 = playlist.get();
            boolean isUserOk = this.isUserInUsersList(playlist1.getUsers(), userId);
            if(isUserOk){
                Song song1 = song.get();
                playlist1.getSongs().add(song1);
                playlistRepository.save(playlist1);
            }
        }
    }

    public void deleteSongFromPlaylist(UUID playlistId, UUID songId, UUID userId){
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        Optional<Song> song = songRepository.findById(songId);
        if(playlist.isPresent() && song.isPresent()){
            Playlist playlist1 = playlist.get();
            boolean isUserOk = this.isUserInUsersList(playlist1.getUsers(), userId);
            if(isUserOk){
                Song song1 = song.get();
                playlist1.getSongs().remove(song1);
                playlistRepository.save(playlist1);
            }
        }

    }

    public List<Song> setSongs(List<UUID> ids){
        List<Song> songs = new ArrayList<>();
        for(UUID id:ids){
            Optional<Song> song = songRepository.findById(id);
            song.ifPresent(songs::add);
        }
        return songs;
    }

}
