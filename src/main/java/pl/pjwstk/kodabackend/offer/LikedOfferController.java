package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.service.LikedOfferService;
import pl.pjwstk.kodabackend.security.user.AppUserService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Slf4j
public class LikedOfferController {

    private final LikedOfferService likedOfferService;
    private final AppUserService appUserService;

    @GetMapping("/liked")
    public ResponseEntity<List<OfferDto>> getLikedOffers() {
        UUID userId = getCurrentUserId();
        log.info("Pobieranie polubionych ofert dla użytkownika: {}", userId);
        List<OfferDto> likedOffers = likedOfferService.getLikedOffersByUserId(userId);
        return ResponseEntity.ok(likedOffers);
    }

    @GetMapping("/{offerId}/liked")
    public ResponseEntity<Boolean> isOfferLiked(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        log.info("Sprawdzanie statusu polubienia oferty {} dla użytkownika {}", offerId, userId);
        boolean isLiked = likedOfferService.isOfferLikedByUser(userId, offerId);
        return ResponseEntity.ok(isLiked);
    }

    @PostMapping("/{offerId}/like")
    public ResponseEntity<Void> likeOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        log.info("Dodawanie oferty {} do ulubionych dla użytkownika {}", offerId, userId);
        likedOfferService.likeOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{offerId}/unlike")
    public ResponseEntity<Void> unlikeOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        log.info("Usuwanie oferty {} z ulubionych dla użytkownika {}", offerId, userId);
        likedOfferService.unlikeOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{offerId}/toggle")
    public ResponseEntity<Void> toggleLikedOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        log.info("Przełączanie statusu polubienia oferty {} dla użytkownika {}", offerId, userId);
        likedOfferService.toggleLikedOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.debug("Pobieranie ID użytkownika dla e-mail: {}", email);
        AppUser user = appUserService.getUserByEmail(email);
        return user.getId();
    }
}