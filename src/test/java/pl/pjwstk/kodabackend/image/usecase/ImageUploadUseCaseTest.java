package pl.pjwstk.kodabackend.image.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.exception.NotFoundException;
import pl.pjwstk.kodabackend.image.model.ImageUploadResponse;
import pl.pjwstk.kodabackend.image.service.FileStorageService;
import pl.pjwstk.kodabackend.image.service.FileValidationService;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageUploadUseCaseTest {

    @Mock
    private OfferImageRepository offerImageRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private FileValidationService validationService;

    @Mock
    private FileStorageService storageService;

    @InjectMocks
    private ImageUploadUseCase imageUploadUseCase;

    private UUID userId;
    private UUID offerId;
    private String userEmail;
    private AppUser user;
    private Offer offer;
    private MultipartFile[] files;
    private OfferImage savedImage;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        offerId = UUID.randomUUID();
        userEmail = "test@example.com";

        user = new AppUser();
        user.setId(userId);
        user.setEmail(userEmail);

        offer = new Offer();
        offer.setId(offerId);
        offer.setSeller(user);

        MockMultipartFile file1 = new MockMultipartFile(
                "file", "test1.jpg", "image/jpeg", "test data".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "test2.jpg", "image/jpeg", "test data 2".getBytes()
        );
        files = new MultipartFile[]{file1, file2};

        savedImage = OfferImage.builder()
                .id(UUID.randomUUID())
                .url("/api/v1/images/view/saved-file.jpg")
                .sortOrder(1)
                .build();
    }

    @Test
    void uploadImagesForOffer_WhenValidRequest_UploadsSuccessfully() throws IOException {
        // given
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerImageRepository.countByOfferId(offerId)).thenReturn(0);
        when(storageService.saveFile(any(MultipartFile.class))).thenReturn("saved-file.jpg");
        when(offerImageRepository.save(any(OfferImage.class))).thenReturn(savedImage);

        // when
        List<ImageUploadResponse> responses = imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail);

        // then
        assertEquals(2, responses.size());
        verify(validationService).validateUploadRequest(files);
        verify(storageService, times(2)).saveFile(any(MultipartFile.class));
        verify(offerImageRepository, times(2)).save(any(OfferImage.class));

        ArgumentCaptor<OfferImage> imageCaptor = ArgumentCaptor.forClass(OfferImage.class);
        verify(offerImageRepository, times(2)).save(imageCaptor.capture());
        List<OfferImage> capturedImages = imageCaptor.getAllValues();
        assertTrue(capturedImages.get(0).isPrimary());
        assertFalse(capturedImages.get(1).isPrimary());
        assertEquals(1, capturedImages.get(0).getSortOrder());
        assertEquals(2, capturedImages.get(1).getSortOrder());
    }

    @Test
    void uploadImagesForOffer_WhenUserNotFound_ThrowsNotFoundException() {
        // given
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail)
        );

        assertEquals("User not found", exception.getMessage());
        verify(validationService).validateUploadRequest(files);
        verifyNoInteractions(offerRepository, storageService);
    }

    @Test
    void uploadImagesForOffer_WhenOfferNotFound_ThrowsNotFoundException() {
        // given
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail)
        );

        assertEquals("Offer not found", exception.getMessage());
        verifyNoInteractions(storageService);
    }

    @Test
    void uploadImagesForOffer_WhenUserDoesntOwnOffer_ThrowsBadRequestException() {
        // given
        AppUser otherUser = new AppUser();
        otherUser.setId(UUID.randomUUID());
        offer.setSeller(otherUser);

        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail)
        );

        assertEquals("You don't have permission to add images to this offer", exception.getMessage());
        verifyNoInteractions(storageService);
    }

    @Test
    void uploadImagesForOffer_WhenStorageThrowsIOException_ThrowsBadRequestException() throws IOException {
        // given
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerImageRepository.countByOfferId(offerId)).thenReturn(0);
        when(storageService.saveFile(any(MultipartFile.class)))
                .thenThrow(new IOException("Storage error"));

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail)
        );

        assertTrue(exception.getMessage().contains("Error saving file"));
        verify(storageService).saveFile(files[0]);
        verify(offerImageRepository, never()).save(any());
    }

    @Test
    void uploadImagesForOffer_WhenExistingImages_SetsCorrectSortOrder() throws IOException {
        // given
        when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerImageRepository.countByOfferId(offerId)).thenReturn(3);
        when(storageService.saveFile(any(MultipartFile.class))).thenReturn("saved-file.jpg");
        when(offerImageRepository.save(any(OfferImage.class))).thenReturn(savedImage);

        // when
        imageUploadUseCase.uploadImagesForOffer(files, offerId, userEmail);

        // then
        ArgumentCaptor<OfferImage> imageCaptor = ArgumentCaptor.forClass(OfferImage.class);
        verify(offerImageRepository, times(2)).save(imageCaptor.capture());
        List<OfferImage> capturedImages = imageCaptor.getAllValues();
        assertFalse(capturedImages.get(0).isPrimary());
        assertFalse(capturedImages.get(1).isPrimary());
        assertEquals(4, capturedImages.get(0).getSortOrder());
        assertEquals(5, capturedImages.get(1).getSortOrder());
    }

    @Test
    void uploadStandaloneImages_WhenValidRequest_UploadsSuccessfully() throws IOException {
        // given
        when(storageService.saveFile(any(MultipartFile.class))).thenReturn("saved-file.jpg");
        when(offerImageRepository.save(any(OfferImage.class))).thenReturn(savedImage);

        // when
        List<ImageUploadResponse> responses = imageUploadUseCase.uploadStandaloneImages(files, userEmail);

        // then
        assertEquals(2, responses.size());
        verify(validationService).validateUploadRequest(files);
        verify(storageService, times(2)).saveFile(any(MultipartFile.class));
        verify(offerImageRepository, times(2)).save(any(OfferImage.class));

        ArgumentCaptor<OfferImage> imageCaptor = ArgumentCaptor.forClass(OfferImage.class);
        verify(offerImageRepository, times(2)).save(imageCaptor.capture());
        List<OfferImage> capturedImages = imageCaptor.getAllValues();
        assertTrue(capturedImages.get(0).isPrimary());
        assertFalse(capturedImages.get(1).isPrimary());
        assertNull(capturedImages.get(0).getOffer());
        assertNull(capturedImages.get(1).getOffer());
    }

    @Test
    void uploadStandaloneImages_WhenStorageThrowsIOException_ThrowsBadRequestException() throws IOException {
        // given
        when(storageService.saveFile(any(MultipartFile.class)))
                .thenThrow(new IOException("Storage error"));

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> imageUploadUseCase.uploadStandaloneImages(files, userEmail)
        );

        assertTrue(exception.getMessage().contains("Error saving file"));
        verify(storageService).saveFile(files[0]);
        verify(offerImageRepository, never()).save(any());
    }
}