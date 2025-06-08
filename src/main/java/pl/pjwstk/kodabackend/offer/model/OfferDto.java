package pl.pjwstk.kodabackend.offer.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Vehicle offer information")
public record OfferDto(
        @Schema(description = "Unique offer identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Offer title", example = "BMW X5 3.0d xDrive - Perfect condition!", required = true)
        String title,

        @Schema(description = "Offer description",
                example = "Selling BMW X5 in excellent technical condition. First owner, serviced at authorized dealer.")
        String description,

        @Schema(description = "Vehicle details")
        CarDetailsDto CarDetailsDto,

        @Schema(description = "List of vehicle image URLs",
                example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        List<String> imageUrls,

        @Schema(description = "Vehicle price", example = "125000.00", minimum = "0")
        BigDecimal price,

        @Schema(description = "Currency", example = "PLN", defaultValue = "PLN")
        String currency,

        @Schema(description = "Seller information")
        UserDto seller,

        @Schema(description = "Vehicle location", example = "Warsaw, Mazovian Voivodeship")
        String location,

        @Schema(description = "Contact phone number", example = "+48 123 456 789")
        String contactPhone,

        @Schema(description = "Contact email", example = "seller@example.com")
        String contactEmail,

        @Schema(description = "Offer creation date", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update date", example = "2024-01-20T14:45:00")
        LocalDateTime updatedAt,

        @Schema(description = "Offer expiration date", example = "2024-04-15T23:59:59")
        LocalDateTime expirationDate,

        @Schema(description = "Number of views", example = "157", minimum = "0")
        int viewCount,

        @Schema(description = "Whether the offer is featured", example = "false")
        boolean featured,

        @Schema(description = "Whether the price is negotiable", example = "true")
        boolean negotiable
) {
}