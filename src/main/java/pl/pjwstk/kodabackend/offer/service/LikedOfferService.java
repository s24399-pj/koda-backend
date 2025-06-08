package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.LikedOffer;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.repository.LikedOfferRepository;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing user's liked/favorite offers.
 * Provides functionality to like, unlike, toggle like status, and retrieve liked offers.
 * <p>
 * This service handles all business logic related to user favorites,
 * including checking like status, adding/removing likes, and fetching user's liked offers.
 * All write operations are transactional to ensure data consistency.
 */
@Service
@RequiredArgsConstructor
public class LikedOfferService {

    private final LikedOfferRepository likedOfferRepository;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Transactional(readOnly = true)
    public List<OfferDto> getLikedOffersByUserId(UUID userId) {
        List<UUID> offerIds = likedOfferRepository.findOfferIdsByUserId(userId);
        List<Offer> offers = offerRepository.findAllById(offerIds);

        return offers.stream()
                .map(offerMapper::mapToOfferDto)
                .toList();
    }

    public boolean isOfferLikedByUser(UUID userId, UUID offerId) {
        return likedOfferRepository.existsByUserIdAndOfferId(userId, offerId);
    }

    @Transactional
    public void likeOffer(UUID userId, UUID offerId) {
        if (likedOfferRepository.existsByUserIdAndOfferId(userId, offerId)) {
            return;
        }

        likedOfferRepository.save(createLikedOffer(userId, offerId));
    }

    @Transactional
    public void unlikeOffer(UUID userId, UUID offerId) {
        likedOfferRepository.deleteByUserIdAndOfferId(userId, offerId);
    }

    @Transactional
    public void toggleLikedOffer(UUID userId, UUID offerId) {
        if (likedOfferRepository.existsByUserIdAndOfferId(userId, offerId)) {
            likedOfferRepository.deleteByUserIdAndOfferId(userId, offerId);
        } else {
            likedOfferRepository.save(createLikedOffer(userId, offerId));
        }
    }

    private LikedOffer createLikedOffer(UUID userId, UUID offerId) {
        return LikedOffer.builder()
                .userId(userId)
                .offerId(offerId)
                .build();
    }
}