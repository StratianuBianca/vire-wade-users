package info.wade.users.service;

import info.wade.users.dto.SongSpotifyDTO;
import info.wade.users.dto.SongsSpotifyDTO;
import info.wade.users.entity.SongSpotify;
import info.wade.users.entity.User;
import info.wade.users.repository.SongSpotifyRepository;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SongSpotifyService {
    @Autowired
    private SongSpotifyRepository songSpotifyRepository;
    @Autowired
    private UserRepository userRepository;

    public SongSpotifyDTO createSongSpotify(SongSpotifyDTO songSpotifyDTO){
        SongSpotify songSpotify = new SongSpotify();
        Optional<User> optionalUser = userRepository.findById(songSpotifyDTO.getUserId());
        Optional<SongSpotify> optionalSongSpotify = songSpotifyRepository.findBySpotifyUrlAndUserId(songSpotifyDTO.getSpotifyUrl(), songSpotifyDTO.getUserId());
        if(optionalUser.isPresent() && optionalSongSpotify.isEmpty()){
            songSpotify.setUrl(songSpotifyDTO.getUrl());
            songSpotify.setArtists(songSpotifyDTO.getArtists());
            songSpotify.setName(songSpotifyDTO.getName());
            songSpotify.setUserId(songSpotifyDTO.getUserId());
            songSpotify.setSpotifyUrl(songSpotifyDTO.getSpotifyUrl());
            songSpotifyRepository.save(songSpotify);
            songSpotifyDTO.setSongSpotify_id(songSpotify.getSongSpotify_id());
            return songSpotifyDTO;
        }
        return new SongSpotifyDTO();
    }

    public List<SongSpotifyDTO> createMultipleSongSpotify(SongsSpotifyDTO songsSpotifyDTOS){
        List<SongSpotifyDTO> songSpotifyDTOList = songsSpotifyDTOS.getSongSpotifyDTOList();
        List<SongSpotifyDTO> updatedList = new ArrayList<>();
        for(SongSpotifyDTO songSpotifyDTO: songSpotifyDTOList){
            SongSpotifyDTO spotifyDTO = createSongSpotify(songSpotifyDTO);
            updatedList.add(spotifyDTO);
        }
        return updatedList;
    }
    public void createMultipleSongsSpotify(List<SongSpotifyDTO> songSpotifyDTOList){
        List<SongSpotifyDTO> updatedList = new ArrayList<>();
        for(SongSpotifyDTO songSpotifyDTO: songSpotifyDTOList){
            SongSpotifyDTO spotifyDTO = createSongSpotify(songSpotifyDTO);
            updatedList.add(spotifyDTO);
        }
    }

//    public SongSpotifyDTO updateSongSpotify(SongSpotifyDTO songSpotifyDTO){
//        SongSpotify songSpotify = new SongSpotify();
//        Optional<User> optionalUser = userRepository.findById(songSpotifyDTO.getUserId());
//        Optional<SongSpotify> optionalArtistSpotify = songSpotifyRepository.findById(songSpotifyDTO.getArtistSpotify_id());
//        if(optionalUser.isPresent() && optionalArtistSpotify.isPresent()){
//            songSpotify.setUrl(songSpotifyDTO.getUrl());
//            songSpotify.setArtists(songSpotifyDTO.getArtists());
//            songSpotify.setName(songSpotifyDTO.getName());
//            songSpotify.setUserId(songSpotifyDTO.getUserId());
//            songSpotifyRepository.save(songSpotify);
//            songSpotifyDTO.setArtistSpotify_id(songSpotify.getSongSpotify_id());
//            return songSpotifyDTO;
//        }
//        return new SongSpotifyDTO();
//    }

    public boolean deleteSongSpotify(UUID songSpotifyId){
        Optional<SongSpotify> optionalSongSpotify = songSpotifyRepository.findById(songSpotifyId);
        if(optionalSongSpotify.isPresent()){
            songSpotifyRepository.delete(optionalSongSpotify.get());
            return true;
        }
        return false;
    }

    public List<SongSpotifyDTO> getAllSongsByUser(UUID userId){
        List<SongSpotifyDTO> songSpotifyDTOS = new ArrayList<>();
        List<SongSpotify> songSpotifyList = songSpotifyRepository.findAll();
        for(SongSpotify songSpotify : songSpotifyList){
            if(songSpotify.getUserId().equals(userId)){
                SongSpotifyDTO songSpotifyDTO = new SongSpotifyDTO();
                songSpotifyDTO.setArtists(songSpotify.getArtists());
                songSpotifyDTO.setUrl(songSpotify.getUrl());
                songSpotifyDTO.setName(songSpotify.getName());
                songSpotifyDTO.setSongSpotify_id(songSpotify.getSongSpotify_id());
                songSpotifyDTO.setUserId(songSpotify.getUserId());
                songSpotifyDTO.setSpotifyUrl(songSpotify.getSpotifyUrl());
                songSpotifyDTOS.add(songSpotifyDTO);

            }
        }
        return songSpotifyDTOS;
    }

}
