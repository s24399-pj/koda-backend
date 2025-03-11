package pl.pjwstk.kodabackend.offer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OfferDto(
        UUID id,
        String title,
        String description,
        CarDetailsDto CarDetailsDto,
        List<String> imageUrls,
        BigDecimal price,
        String currency,
        String location,
        String contactPhone,
        String contactEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime expirationDate,
        int viewCount,
        boolean featured,
        boolean negotiable
){}
