package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.CreateOfferCommand;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.AppUserService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final AppUserService appUserService;

    @Transactional(readOnly = true)
    public OfferDto findOfferById(UUID id) {
        return offerRepository.findByIdWithDetails(id)
                .map(offerMapper::mapToOfferDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Article not found with id: ", id.toString())
                );
    }

    @Transactional(readOnly = true)
    public List<String> findOfferNamesByPhrase(String phrase) {
        return offerRepository.findDistinctTitlesByPhrase(phrase);
    }

    @Transactional
    public OfferDto createOffer(CreateOfferCommand command, String userEmail) {
        AppUser user = appUserService.getUserByEmail(userEmail);

        Offer offer = offerMapper.mapToOffer(command);

        offer.setSeller(user);

        LocalDateTime now = LocalDateTime.now();

        if (offer.getExpirationDate() == null) {
            offer.setExpirationDate(now.plusDays(30));
        }

        offer.setViewCount(0);

        Offer savedOffer = offerRepository.save(offer);

        return offerMapper.mapToOfferDto(savedOffer);
    }

    /**
     * Usuwa ofertę o określonym identyfikatorze.
     * Tylko właściciel oferty może ją usunąć.
     *
     * @param offerId ID oferty do usunięcia
     * @param userEmail email użytkownika próbującego usunąć ofertę
     * @throws EntityNotFoundException gdy oferta o podanym ID nie istnieje
     * @throws AccessDeniedException gdy użytkownik nie jest właścicielem oferty
     */
    @Transactional
    public void deleteOffer(UUID offerId, String userEmail) {
        log.info("Próba usunięcia oferty o ID: {} przez użytkownika: {}", offerId, userEmail);

        // Pobierz użytkownika na podstawie emaila
        AppUser user = appUserService.getUserByEmail(userEmail);

        // Pobierz ofertę wraz ze szczegółami
        Offer offer = offerRepository.findByIdWithDetails(offerId)
                .orElseThrow(() -> {
                    log.warn("Próba usunięcia nieistniejącej oferty o ID: {}", offerId);
                    return new EntityNotFoundException("Offer not found with id: ", offerId.toString());
                });

        // Sprawdź, czy użytkownik jest właścicielem oferty
        if (!offer.getSeller().getId().equals(user.getId())) {
            log.warn("Użytkownik {} próbuje usunąć ofertę {} należącą do innego użytkownika",
                    userEmail, offerId);
            throw new AccessDeniedException("User is not authorized to delete this offer");
        }

        log.info("Usuwanie oferty o ID: {}", offerId);
        offerRepository.delete(offer);
        log.info("Oferta o ID: {} została pomyślnie usunięta", offerId);
    }
}