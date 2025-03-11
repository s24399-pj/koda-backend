package pl.pjwstk.kodabackend.offer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OfferMapper {
    private final CarDetailsMapper carDetailsMapper;

    public OfferDto mapToOfferDto(Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                carDetailsMapper.mapToCarDetailsDto(offer.getCarDetails()),
                extractImageUrls(offer.getImages()),
                offer.getPrice(),
                offer.getCurrency(),
                offer.getLocation(),
                offer.getContactPhone(),
                offer.getContactEmail(),
                offer.getCreatedAt(),
                offer.getUpdatedAt(),
                offer.getExpirationDate(),
                offer.getViewCount(),
                offer.isFeatured(),
                offer.isNegotiable()
        );
    }

    private List<String> extractImageUrls(List<OfferImage> images) {
        return images.stream()
                .map(OfferImage::getUrl)
                .collect(Collectors.toList());
    }
}
