package pl.pjwstk.kodabackend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Complete user profile information")
public class AppUserDto {

    @Schema(
            description = "Unique identifier of the user",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID id;

    @Schema(
            description = "User's first name",
            example = "John",
            minLength = 1,
            maxLength = 100
    )
    private String firstName;

    @Schema(
            description = "User's last name",
            example = "Doe",
            minLength = 1,
            maxLength = 100
    )
    private String lastName;

    @Schema(
            description = "User's email address",
            example = "john.doe@example.com",
            format = "email"
    )
    private String email;

    @Schema(
            description = "User's profile picture as base64 encoded byte array",
            nullable = true
    )
    private byte[] profilePicture;
}