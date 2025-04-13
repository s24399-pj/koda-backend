package pl.pjwstk.kodabackend.security.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AppUserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private byte[] profilePicture;
}