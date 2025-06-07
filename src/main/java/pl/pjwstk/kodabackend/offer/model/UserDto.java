package pl.pjwstk.kodabackend.offer.model;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String profilePictureBase64
) {
}