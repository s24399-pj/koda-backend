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
        List<OfferDto> likedOffers = likedOfferService.getLikedOffersByUserId(userId);
        return ResponseEntity.ok(likedOffers);
    }

    @GetMapping("/{offerId}/liked")
    public ResponseEntity<Boolean> isOfferLiked(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        boolean isLiked = likedOfferService.isOfferLikedByUser(userId, offerId);
        return ResponseEntity.ok(isLiked);
    }

    @PostMapping("/{offerId}/like")
    public ResponseEntity<Void> likeOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        likedOfferService.likeOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{offerId}/unlike")
    public ResponseEntity<Void> unlikeOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        log.info("Removing offer {} from favorites for user {}", offerId, userId);
        likedOfferService.unlikeOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{offerId}/toggle")
    public ResponseEntity<Void> toggleLikedOffer(@PathVariable UUID offerId) {
        UUID userId = getCurrentUserId();
        likedOfferService.toggleLikedOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.getUserByEmail(email);
        return user.getId();
    }
}