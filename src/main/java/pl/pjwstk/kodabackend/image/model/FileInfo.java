package pl.pjwstk.kodabackend.image.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(description = "File information including metadata")
public record FileInfo(
        @Schema(description = "Name of the file", example = "car-image-123.jpg")
        String filename,

        @Schema(description = "File size in bytes", example = "2048576")
        long size,

        @Schema(description = "Last modification timestamp", example = "2024-01-15T10:30:00Z")
        Instant lastModified,

        @Schema(description = "MIME content type", example = "image/jpeg")
        String contentType
) {
}