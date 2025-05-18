package pl.pjwstk.kodabackend.offer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedOfferDto {
    private UUID id;
    private UUID userId;
    private UUID offerId;
    private LocalDateTime createdAt;
}