package pl.pjwstk.kodabackend.offer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.offer.controller.LikedOfferController;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.service.LikedOfferService;
import pl.pjwstk.kodabackend.security.user.AppUserService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
@Validated
class LikedOfferControllerImpl implements LikedOfferController {

    private final LikedOfferService likedOfferService;
    private final AppUserService appUserService;

    @GetMapping("/liked")
    @Override
    public List<OfferDto> getLikedOffers(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        log.debug("Fetching liked offers for user: {}", userId);
        List<OfferDto> likedOffers = likedOfferService.getLikedOffersByUserId(userId);
        log.debug("Found {} liked offers for user: {}", likedOffers.size(), userId);
        return likedOffers;
    }

    @GetMapping("/{offerId}/liked")
    @Override
    public Boolean isOfferLiked(@PathVariable UUID offerId, Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        log.debug("Checking if offer {} is liked by user: {}", offerId, userId);
        boolean isLiked = likedOfferService.isOfferLikedByUser(userId, offerId);
        log.debug("Offer {} is {} by user: {}", offerId, isLiked ? "liked" : "not liked", userId);
        return isLiked;
    }

    @PostMapping("/{offerId}/like")
    @Override
    public void likeOffer(@PathVariable UUID offerId, Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        log.debug("User {} is liking offer: {}", userId, offerId);
        likedOfferService.likeOffer(userId, offerId);
        log.debug("Successfully liked offer {} by user: {}", offerId, userId);
    }

    @PostMapping("/{offerId}/unlike")
    @Override
    public void unlikeOffer(@PathVariable UUID offerId, Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        log.debug("User {} is unliking offer: {}", userId, offerId);
        likedOfferService.unlikeOffer(userId, offerId);
        log.debug("Successfully unliked offer {} by user: {}", offerId, userId);
    }

    @PostMapping("/{offerId}/toggle")
    @Override
    public void toggleLikedOffer(@PathVariable UUID offerId, Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        log.debug("User {} is toggling like status for offer: {}", userId, offerId);
        likedOfferService.toggleLikedOffer(userId, offerId);
        log.debug("Successfully toggled like status for offer {} by user: {}", offerId, userId);
    }

    private UUID getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        AppUser user = appUserService.getUserByEmail(email);
        return user.getId();
    }
}