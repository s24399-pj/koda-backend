package pl.pjwstk.kodabackend.offer;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

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
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> offerService.findOfferById(testId)
        );

        // Verify error message and mock interactions
        assertTrue(exception.getMessage().contains("not found with id: " + testId));
        verify(offerRepository).findByIdWithDetails(testId);
        verify(offerMapper, never()).mapToOfferDto(any());
    }
}