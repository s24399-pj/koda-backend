package pl.pjwstk.kodabackend.offer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO containing full details about an offer, including all car details and equipment
 */
public record OfferDetailedDto(
        UUID id,
        String title,
        String description,
        CarDetailsDto carDetails,
        List<String> imageUrls,
        BigDecimal price,
        String currency,
        UserDto seller,
        String location,
        String contactPhone,
        String contactEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime expirationDate,
        int viewCount,
        boolean featured,
        boolean negotiable
) {
}