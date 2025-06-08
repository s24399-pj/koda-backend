package pl.pjwstk.kodabackend.offer.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "User information")
public record UserDto(
        @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Base64 encoded profile picture",
                example = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD...")
        String profilePictureBase64
) {
}