package info.wade.users.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginReturn {
    private String jwt;
    private UUID id;
    private String username;
}
