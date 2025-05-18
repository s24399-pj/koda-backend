package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.LikedOffer;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.repository.LikedOfferRepository;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikedOfferService {

    private final LikedOfferRepository likedOfferRepository;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Transactional(readOnly = true)
    public List<OfferDto> getLikedOffersByUserId(UUID userId) {
        log.info("Pobieranie ID polubionych ofert dla użytkownika: {}", userId);
        List<UUID> offerIds = likedOfferRepository.findOfferIdsByUserId(userId);
        log.info("Znaleziono {} polubionych ofert", offerIds.size());

        List<Offer> offers = offerRepository.findAllById(offerIds);
        return offers.stream()
                .map(offerMapper::mapToOfferDto)
                .collect(Collectors.toList());
    }

    public boolean isOfferLikedByUser(UUID userId, UUID offerId) {
        boolean isLiked = likedOfferRepository.existsByUserIdAndOfferId(userId, offerId);
        log.info("Oferta {} jest polubiona przez użytkownika {}: {}", offerId, userId, isLiked);
        return isLiked;
    }

    @Transactional
    public void likeOffer(UUID userId, UUID offerId) {
        log.info("Dodawanie oferty {} do ulubionych dla użytkownika {}", offerId, userId);
        if (likedOfferRepository.existsByUserIdAndOfferId(userId, offerId)) {
            log.info("Oferta jest już polubiona - nic nie robię");
            return;
        }

        LikedOffer likedOffer = LikedOffer.builder()
                .userId(userId)
                .offerId(offerId)
                .build();

        likedOfferRepository.save(likedOffer);
        log.info("Oferta {} została dodana do ulubionych dla użytkownika {}", offerId, userId);
    }

    @Transactional
    public void unlikeOffer(UUID userId, UUID offerId) {
        log.info("Usuwanie oferty {} z ulubionych dla użytkownika {}", offerId, userId);
        likedOfferRepository.deleteByUserIdAndOfferId(userId, offerId);
        log.info("Oferta {} została usunięta z ulubionych dla użytkownika {}", offerId, userId);
    }

    @Transactional
    public void toggleLikedOffer(UUID userId, UUID offerId) {
        log.info("Przełączanie statusu polubienia oferty {} dla użytkownika {}", offerId, userId);
        boolean exists = likedOfferRepository.existsByUserIdAndOfferId(userId, offerId);

        if (exists) {
            log.info("Oferta jest polubiona - usuwam polubienie");
            likedOfferRepository.deleteByUserIdAndOfferId(userId, offerId);
        } else {
            log.info("Oferta nie jest polubiona - dodaję polubienie");
            LikedOffer likedOffer = LikedOffer.builder()
                    .userId(userId)
                    .offerId(offerId)
                    .build();

            likedOfferRepository.save(likedOffer);
        }
    }
}