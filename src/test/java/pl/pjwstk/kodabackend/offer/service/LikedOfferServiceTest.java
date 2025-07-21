package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.LikedOffer;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.repository.LikedOfferRepository;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikedOfferServiceTest {

    @Mock
    private LikedOfferRepository likedOfferRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private LikedOfferService likedOfferService;

    private UUID userId;
    private UUID offerId1;
    private UUID offerId2;
    private Offer offer1;
    private Offer offer2;
    private OfferDto offerDto1;
    private OfferDto offerDto2;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        offerId1 = UUID.randomUUID();
        offerId2 = UUID.randomUUID();

        offer1 = new Offer();
        offer1.setId(offerId1);
        offer1.setTitle("BMW X5");

        offer2 = new Offer();
        offer2.setId(offerId2);
        offer2.setTitle("Audi A4");

        offerDto1 = mock(OfferDto.class);
        offerDto2 = mock(OfferDto.class);
    }

    @Test
    void getLikedOffersByUserId_WhenUserHasLikedOffers_ReturnsOfferDtos() {
        // given
        List<UUID> likedOfferIds = Arrays.asList(offerId1, offerId2);
        List<Offer> offers = Arrays.asList(offer1, offer2);

        when(likedOfferRepository.findOfferIdsByUserId(userId)).thenReturn(likedOfferIds);
        when(offerRepository.findAllById(likedOfferIds)).thenReturn(offers);
        when(offerMapper.mapToOfferDto(offer1)).thenReturn(offerDto1);
        when(offerMapper.mapToOfferDto(offer2)).thenReturn(offerDto2);

        // when
        List<OfferDto> result = likedOfferService.getLikedOffersByUserId(userId);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(offerDto1));
        assertTrue(result.contains(offerDto2));
        verify(likedOfferRepository).findOfferIdsByUserId(userId);
        verify(offerRepository).findAllById(likedOfferIds);
        verify(offerMapper).mapToOfferDto(offer1);
        verify(offerMapper).mapToOfferDto(offer2);
    }

    @Test
    void getLikedOffersByUserId_WhenUserHasNoLikedOffers_ReturnsEmptyList() {
        // given
        when(likedOfferRepository.findOfferIdsByUserId(userId)).thenReturn(Collections.emptyList());
        when(offerRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());

        // when
        List<OfferDto> result = likedOfferService.getLikedOffersByUserId(userId);

        // then
        assertTrue(result.isEmpty());
        verify(likedOfferRepository).findOfferIdsByUserId(userId);
        verify(offerRepository).findAllById(Collections.emptyList());
        verifyNoInteractions(offerMapper);
    }

    @Test
    void isOfferLikedByUser_WhenOfferIsLiked_ReturnsTrue() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(true);

        // when
        boolean result = likedOfferService.isOfferLikedByUser(userId, offerId1);

        // then
        assertTrue(result);
        verify(likedOfferRepository).existsByUserIdAndOfferId(userId, offerId1);
    }

    @Test
    void isOfferLikedByUser_WhenOfferIsNotLiked_ReturnsFalse() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(false);

        // when
        boolean result = likedOfferService.isOfferLikedByUser(userId, offerId1);

        // then
        assertFalse(result);
        verify(likedOfferRepository).existsByUserIdAndOfferId(userId, offerId1);
    }

    @Test
    void likeOffer_WhenOfferNotYetLiked_SavesLikedOffer() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(false);

        // when
        likedOfferService.likeOffer(userId, offerId1);

        // then
        ArgumentCaptor<LikedOffer> captor = ArgumentCaptor.forClass(LikedOffer.class);
        verify(likedOfferRepository).save(captor.capture());

        LikedOffer savedLikedOffer = captor.getValue();
        assertEquals(userId, savedLikedOffer.getUserId());
        assertEquals(offerId1, savedLikedOffer.getOfferId());
    }

    @Test
    void likeOffer_WhenOfferAlreadyLiked_DoesNothing() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(true);

        // when
        likedOfferService.likeOffer(userId, offerId1);

        // then
        verify(likedOfferRepository, never()).save(any());
        verify(likedOfferRepository).existsByUserIdAndOfferId(userId, offerId1);
    }

    @Test
    void unlikeOffer_WhenCalled_DeletesLikedOffer() {
        // when
        likedOfferService.unlikeOffer(userId, offerId1);

        // then
        verify(likedOfferRepository).deleteByUserIdAndOfferId(userId, offerId1);
    }

    @Test
    void toggleLikedOffer_WhenOfferIsLiked_RemovesLike() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(true);

        // when
        likedOfferService.toggleLikedOffer(userId, offerId1);

        // then
        verify(likedOfferRepository).deleteByUserIdAndOfferId(userId, offerId1);
        verify(likedOfferRepository, never()).save(any());
    }

    @Test
    void toggleLikedOffer_WhenOfferIsNotLiked_AddsLike() {
        // given
        when(likedOfferRepository.existsByUserIdAndOfferId(userId, offerId1)).thenReturn(false);

        // when
        likedOfferService.toggleLikedOffer(userId, offerId1);

        // then
        ArgumentCaptor<LikedOffer> captor = ArgumentCaptor.forClass(LikedOffer.class);
        verify(likedOfferRepository).save(captor.capture());
        verify(likedOfferRepository, never()).deleteByUserIdAndOfferId(any(), any());

        LikedOffer savedLikedOffer = captor.getValue();
        assertEquals(userId, savedLikedOffer.getUserId());
        assertEquals(offerId1, savedLikedOffer.getOfferId());
    }
}