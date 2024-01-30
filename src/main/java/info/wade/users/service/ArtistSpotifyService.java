package info.wade.users.service;

import info.wade.users.dto.ArtistSpotifyDTO;
import info.wade.users.dto.ArtistsSpotifyDTO;
import info.wade.users.entity.ArtistSpotify;
import info.wade.users.entity.User;
import info.wade.users.repository.ArtistSpotifyRepository;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArtistSpotifyService {

    @Autowired
    private ArtistSpotifyRepository artistSpotifyRepository;
    @Autowired
    private UserRepository userRepository;

    public ArtistSpotifyDTO createArtistSpotify(ArtistSpotifyDTO artistSpotifyDTO){
        ArtistSpotify artistSpotify = new ArtistSpotify();
        Optional<User> optionalUser = userRepository.findById(artistSpotifyDTO.getUserId());
        Optional<ArtistSpotify> optionalArtistSpotify = artistSpotifyRepository.findByUserIdAndUrlSpotify(artistSpotifyDTO.getUserId(), artistSpotifyDTO.getUrlSpotify());
        if(optionalUser.isPresent() && optionalArtistSpotify.isEmpty()){
            artistSpotify.setUrl(artistSpotifyDTO.getUrl());
            artistSpotify.setUrlSpotify(artistSpotifyDTO.getUrlSpotify());
            artistSpotify.setGenres(artistSpotifyDTO.getGenres());
            artistSpotify.setUserId(artistSpotifyDTO.getUserId());
            artistSpotifyRepository.save(artistSpotify);
            artistSpotifyDTO.setArtistSpotify_id(artistSpotify.getArtistSpotify_id());
            return artistSpotifyDTO;
        }
        return new ArtistSpotifyDTO();
    }

    public List<ArtistSpotifyDTO> createMultipleArtistSpotify(ArtistsSpotifyDTO artistsSpotifyDTO){
        List<ArtistSpotifyDTO> artistSpotifyDTOList = artistsSpotifyDTO.getArtistSpotifyDTOList();
        List<ArtistSpotifyDTO> updatedList = new ArrayList<>();
        for(ArtistSpotifyDTO artistSpotifyDTO: artistSpotifyDTOList){
            ArtistSpotifyDTO spotifyDTO = createArtistSpotify(artistSpotifyDTO);
            updatedList.add(spotifyDTO);
        }
        return updatedList;
    }
    public void createMultipleArtistsSpotify(List<ArtistSpotifyDTO> artistSpotifyDTOList){
        List<ArtistSpotifyDTO> updatedList = new ArrayList<>();
        for(ArtistSpotifyDTO artistSpotifyDTO: artistSpotifyDTOList){
            ArtistSpotifyDTO spotifyDTO = createArtistSpotify(artistSpotifyDTO);
            updatedList.add(spotifyDTO);
        }
    }

    public List<ArtistSpotifyDTO> getAllArtistByUser(UUID userId){
        List<ArtistSpotifyDTO> artistSpotifyDTOS = new ArrayList<>();
        List<ArtistSpotify> artistSpotifyList = artistSpotifyRepository.findAll();
        for(ArtistSpotify artistSpotify : artistSpotifyList){
            if(artistSpotify.getUserId().equals(userId)){
                ArtistSpotifyDTO artistSpotifyDTO = new ArtistSpotifyDTO();
                artistSpotifyDTO.setArtistSpotify_id(artistSpotify.getArtistSpotify_id());
                artistSpotifyDTO.setUrlSpotify(artistSpotify.getUrlSpotify());
                artistSpotifyDTO.setGenres(artistSpotify.getGenres());
                artistSpotifyDTO.setUrl(artistSpotify.getUrl());
                artistSpotifyDTO.setUserId(artistSpotify.getUserId());

                artistSpotifyDTOS.add(artistSpotifyDTO);

            }
        }
        return artistSpotifyDTOS;
    }

    public boolean deleteArtistSpotify(UUID artistSpotifyId){
        Optional<ArtistSpotify> optionalArtistSpotify = artistSpotifyRepository.findById(artistSpotifyId);
        if(optionalArtistSpotify.isPresent()){
            artistSpotifyRepository.delete(optionalArtistSpotify.get());
            return true;
        }
        return false;
    }
}
