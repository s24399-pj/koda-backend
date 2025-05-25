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
public class UserMiniDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
}
