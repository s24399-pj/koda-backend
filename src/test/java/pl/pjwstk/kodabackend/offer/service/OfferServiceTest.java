package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.AppUserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private OfferService offerService;

    private UUID testId;
    private Offer testOffer;
    private OfferDto testOfferDto;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testOffer = new Offer();
        testOfferDto = mock(OfferDto.class);
    }

    @Test
    void findOfferById_WhenOfferExists_ReturnsOfferDto() {
        // Arrange
        when(offerRepository.findByIdWithDetails(testId)).thenReturn(Optional.of(testOffer));
        when(offerMapper.mapToOfferDto(testOffer)).thenReturn(testOfferDto);

        // Act
        OfferDto result = offerService.findOfferById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testOfferDto, result);
        verify(offerRepository).findByIdWithDetails(testId);
        verify(offerMapper).mapToOfferDto(testOffer);
    }

    @Test
    void findOfferById_WhenOfferDoesNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        when(offerRepository.findByIdWithDetails(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> offerService.findOfferById(testId)
        );

        // Verify mock interactions
        verify(offerRepository).findByIdWithDetails(testId);
        verify(offerMapper, never()).mapToOfferDto(any());
    }
}