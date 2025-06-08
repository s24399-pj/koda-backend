package pl.pjwstk.kodabackend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Schema(description = "Basic user information")
public class UserMiniDto {
    @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "User's first name", example = "Jan")
    private String firstName;

    @Schema(description = "User's last name", example = "Nowak")
    private String lastName;

    @Schema(description = "User's full name", example = "Jan Nowak")
    private String fullName;
}