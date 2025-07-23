package pl.pjwstk.kodabackend.image.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.exception.NotFoundException;
import pl.pjwstk.kodabackend.image.service.FileStorageService;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteImageUseCaseTest {

    @Mock
    private OfferImageRepository offerImageRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private FileStorageService storageService;

    @InjectMocks
    private DeleteImageUseCase deleteImageUseCase;

    private UUID imageId;
    private UUID userId;
    private UUID otherUserId;
    private String userEmail;
    private AppUser user;
    private AppUser otherUser;
    private OfferImage offerImage;
    private Offer offer;

    @BeforeEach
    void setUp() {
        imageId = UUID.randomUUID();
        userId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
        userEmail = "test@example.com";

        user = new AppUser();
        user.setId(userId);
        user.setEmail(userEmail);

        otherUser = new AppUser();
        otherUser.setId(otherUserId);

        offer = new Offer();
        offer.setSeller(user);

        offerImage = new OfferImage();
        offerImage.setId(imageId);
        offerImage.setUrl("uploads/test-image.jpg");
        offerImage.setOffer(offer);
    }

    @Test
    void deleteImage_WhenValidUserAndImage_DeletesSuccessfully() throws IOException {
        // Arrange
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.of(offerImage));
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        doNothing().when(storageService).deleteFile("uploads/test-image.jpg");

        // Act
        deleteImageUseCase.deleteImage(imageId, userEmail);

        // Assert
        verify(offerImageRepository).findById(imageId);
        verify(appUserRepository).findByEmail(userEmail);
        verify(storageService).deleteFile("uploads/test-image.jpg");
        verify(offerImageRepository).delete(offerImage);
    }

    @Test
    void deleteImage_WhenImageNotFound_ThrowsNotFoundException() {
        // Arrange
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> deleteImageUseCase.deleteImage(imageId, userEmail)
        );

        assertEquals("Image not found", exception.getMessage());
        verify(offerImageRepository).findById(imageId);
        verifyNoInteractions(appUserRepository, storageService);
    }

    @Test
    void deleteImage_WhenUserNotFound_ThrowsNotFoundException() {
        // Arrange
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.of(offerImage));
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> deleteImageUseCase.deleteImage(imageId, userEmail)
        );

        assertEquals("User not found", exception.getMessage());
        verify(appUserRepository).findByEmail(userEmail);
        verifyNoInteractions(storageService);
    }

    @Test
    void deleteImage_WhenUserNotOwner_ThrowsBadRequestException() {
        // Arrange
        offer.setSeller(otherUser); // Different user owns the offer
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.of(offerImage));
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> deleteImageUseCase.deleteImage(imageId, userEmail)
        );

        assertEquals("You don't have permission to delete this image", exception.getMessage());
        verifyNoInteractions(storageService);
        verify(offerImageRepository, never()).delete(any());
    }

    @Test
    void deleteImage_WhenStorageServiceThrowsIOException_ThrowsBadRequestException() throws IOException {
        // Arrange
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.of(offerImage));
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        doThrow(new IOException("Storage error")).when(storageService).deleteFile(anyString());

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> deleteImageUseCase.deleteImage(imageId, userEmail)
        );

        assertEquals("Error deleting file", exception.getMessage());
        verify(storageService).deleteFile("uploads/test-image.jpg");
        verify(offerImageRepository, never()).delete(any());
    }

    @Test
    void deleteImage_WhenImageHasNoOffer_AllowsDeletion() throws IOException {
        // Arrange
        offerImage.setOffer(null); // Image not associated with any offer
        when(offerImageRepository.findById(imageId)).thenReturn(Optional.of(offerImage));
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        doNothing().when(storageService).deleteFile("uploads/test-image.jpg");

        // Act
        deleteImageUseCase.deleteImage(imageId, userEmail);

        // Assert
        verify(storageService).deleteFile("uploads/test-image.jpg");
        verify(offerImageRepository).delete(offerImage);
    }
}