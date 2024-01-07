package info.wade.users.controller;

import info.wade.users.dto.SpotifyDTO;
import info.wade.users.service.SpotifyService;
import info.wade.users.service.UserService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> spotifyLogin() {
        SpotifyApi object = spotifyService.getSpotifyObject();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
                .scope("app-remote-control user-library-read user-read-recently-played user-read-playback-state playlist-read-private user-read-playback-state user-read-private user-read-email user-top-read")
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();
        SpotifyDTO spotifyDTO = new SpotifyDTO();
        spotifyDTO.setUri(uri.toString());
        return new ResponseEntity<>(spotifyDTO, HttpStatus.OK);
    }

    @PutMapping("/getToken")
    public void getUserSpotifyToken(@RequestParam("code") String token, @RequestParam("userId") UUID userId){
        SpotifyApi object = spotifyService.getSpotifyObject();
        AuthorizationCodeRequest authorizationCodeRequest = object.authorizationCode(token).build();
        User user = null;
        try {
            final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();

            object.setAccessToken(authorizationCode.getAccessToken());
            object.setRefreshToken(authorizationCode.getRefreshToken());

            final GetCurrentUsersProfileRequest getCurrentUsersProfile = object.getCurrentUsersProfile().build();
            user = getCurrentUsersProfile.execute();

            userService.setSpotifyToken(userId, authorizationCode.getAccessToken(), authorizationCode.getRefreshToken());

        } catch (Exception e) {
            System.out.println("Exception occured while getting user code: " + e);
        }
    }

    @GetMapping(value = "/verify")
    public boolean verifySpotifyToken(@RequestParam UUID userId){
        info.wade.users.entity.User user = userService.getUserById(userId);
        if(user.getSpotifyToken() == null)
            return false;
        Date date = user.getSpotifyExpirationToken();
        Date currentTime = new Date();
        long hours = TimeUnit.MILLISECONDS.toHours(currentTime.getTime());
        long hours2 = TimeUnit.MILLISECONDS.toHours(date.getTime());
        return hours != hours2 +1;
    }

    @GetMapping(value = "/userTopSongs")
    public Track[] getUserTopTracks(@RequestParam UUID userId) {
        info.wade.users.entity.User user = userService.getUserById(userId);

        SpotifyApi object = spotifyService.getSpotifyObject();
        object.setAccessToken(user.getSpotifyToken());
        object.setRefreshToken(user.getRefreshToken());

        final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

        try {
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

            return trackPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching top songs: " + e);
        }

        return new Track[0];
    }


    @GetMapping(value = "/userTopArtists")
    public Artist[] getUserTopArtist(@RequestParam UUID userId) throws IOException, ParseException, SpotifyWebApiException {
        info.wade.users.entity.User user = userService.getUserById(userId);

        SpotifyApi object = spotifyService.getSpotifyObject();
        object.setAccessToken(user.getSpotifyToken());
        object.setRefreshToken(user.getRefreshToken());

        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = object.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(5)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            return artistPaging.getItems();
        }catch (Exception e){
            System.out.println("exception");
        }
        return new Artist[0];

    }

    @GetMapping(value = "/userSavedAlbum")
    public SavedAlbum[] getCurrentUserSavedAlbum(@RequestParam UUID userId) {
        info.wade.users.entity.User user = userService.getUserById(userId);

        SpotifyApi object = spotifyService.getSpotifyObject();
        object.setAccessToken(user.getSpotifyToken());
        object.setRefreshToken(user.getRefreshToken());

        final GetCurrentUsersSavedAlbumsRequest getUsersTopArtistsRequest = object.getCurrentUsersSavedAlbums()
                .limit(50)
                .offset(0)
                .build();

        try {
            final Paging<SavedAlbum> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user saved album: " + e);
        }

        return new SavedAlbum[0];
    }
}
