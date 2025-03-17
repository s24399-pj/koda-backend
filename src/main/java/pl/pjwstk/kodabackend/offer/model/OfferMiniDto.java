package pl.pjwstk.kodabackend.offer.model;

import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;

import java.math.BigDecimal;
import java.util.UUID;

public record OfferMiniDto(
        UUID id,
        String title,
        BigDecimal price,
        String mainImage,
        Integer mileage,
        FuelType fuelType,
        Integer year,
        Integer enginePower,
        String displacement
) {
}
