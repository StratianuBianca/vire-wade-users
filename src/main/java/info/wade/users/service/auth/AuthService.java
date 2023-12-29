package info.wade.users.service.auth;

import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UpdateUserDTO;
import info.wade.users.dto.UserDTO;

import java.util.UUID;

public interface AuthService {
    UserDTO createUser(RegisterDTO registerDTO);
    RegisterDTO updateUser(UpdateUserDTO updateUserDTO);
    UpdateUserDTO getUser(UUID id);
}
