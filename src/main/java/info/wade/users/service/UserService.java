package info.wade.users.service;

import info.wade.users.entity.User;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void setSpotifyToken(UUID userId, String token, String refreshToken) {
        Optional<User> queryResult = userRepository.findById(userId);
        if(queryResult.isPresent()){
            User user = queryResult.get();
            user.setSpotifyToken(token);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

    }

    public User getUserById(UUID userId){
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }
}
