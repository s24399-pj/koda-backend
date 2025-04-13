package pl.pjwstk.kodabackend.offer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.model.UserDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.Base64;
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
                mapToUserDto(offer.getSeller()),
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

    private UserDto mapToUserDto(AppUser seller) {
        if (seller == null) {
            return null;
        }

        String profilePictureBase64 = null;
        if (seller.getProfilePicture() != null && seller.getProfilePicture().length > 0) {
            profilePictureBase64 = "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(seller.getProfilePicture());
        }

        return new UserDto(
                seller.getId(),
                seller.getFirstName(),
                seller.getLastName(),
                seller.getEmail(),
                profilePictureBase64
        );
    }

    private List<String> extractImageUrls(List<OfferImage> images) {
        return images.stream()
                .map(OfferImage::getUrl)
                .toList();
    }
}