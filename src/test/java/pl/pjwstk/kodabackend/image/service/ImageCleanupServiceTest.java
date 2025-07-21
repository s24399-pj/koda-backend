package pl.pjwstk.kodabackend.image.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageCleanupServiceTest {

    @Mock
    private OfferImageRepository offerImageRepository;

    @Mock
    private FileStorageService storageService;

    @Mock
    private Path mockPath;

    @InjectMocks
    private ImageCleanupService imageCleanupService;

    private OfferImage image1;
    private OfferImage image2;

    @BeforeEach
    void setUp() {
        image1 = new OfferImage();
        image1.setUrl("/api/v1/images/view/used-file1.jpg");

        image2 = new OfferImage();
        image2.setUrl("/api/v1/images/view/used-file2.jpg");
    }

    @Test
    void cleanupUnusedFiles_WhenUnusedFilesExist_DeletesThem() throws IOException {
        // given
        List<String> allFiles = Arrays.asList("used-file1.jpg", "used-file2.jpg", "unused-file1.jpg", "unused-file2.jpg");
        List<OfferImage> usedImages = Arrays.asList(image1, image2);

        when(storageService.listAllFiles()).thenReturn(allFiles);
        when(offerImageRepository.findAll()).thenReturn(usedImages);
        when(storageService.getFilePath(anyString())).thenReturn(mockPath);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

            // when
            int deletedCount = imageCleanupService.cleanupUnusedFiles();

            // then
            assertEquals(2, deletedCount);
            verify(storageService).getFilePath("unused-file1.jpg");
            verify(storageService).getFilePath("unused-file2.jpg");
            filesMock.verify(() -> Files.deleteIfExists(mockPath), times(2));
        }
    }

    @Test
    void cleanupUnusedFiles_WhenAllFilesAreUsed_DeletesNothing() throws IOException {
        // given
        List<String> allFiles = Arrays.asList("used-file1.jpg", "used-file2.jpg");
        List<OfferImage> usedImages = Arrays.asList(image1, image2);

        when(storageService.listAllFiles()).thenReturn(allFiles);
        when(offerImageRepository.findAll()).thenReturn(usedImages);

        // when
        int deletedCount = imageCleanupService.cleanupUnusedFiles();

        // then
        assertEquals(0, deletedCount);
        verify(storageService, never()).getFilePath(anyString());
    }

    @Test
    void cleanupUnusedFiles_WhenNoFiles_ReturnsZero() throws IOException {
        // given
        when(storageService.listAllFiles()).thenReturn(Collections.emptyList());
        when(offerImageRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        int deletedCount = imageCleanupService.cleanupUnusedFiles();

        // then
        assertEquals(0, deletedCount);
        verify(storageService, never()).getFilePath(anyString());
    }

    @Test
    void cleanupUnusedFiles_WhenIOExceptionDuringDelete_ContinuesAndReturnsPartialCount() throws IOException {
        // given
        List<String> allFiles = Arrays.asList("used-file1.jpg", "unused-file1.jpg", "unused-file2.jpg");
        List<OfferImage> usedImages = Collections.singletonList(image1);

        when(storageService.listAllFiles()).thenReturn(allFiles);
        when(offerImageRepository.findAll()).thenReturn(usedImages);
        when(storageService.getFilePath("unused-file1.jpg")).thenThrow(new IOException("Cannot access file"));
        when(storageService.getFilePath("unused-file2.jpg")).thenReturn(mockPath);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(mockPath)).thenReturn(true);

            // when
            int deletedCount = imageCleanupService.cleanupUnusedFiles();

            // then
            assertEquals(1, deletedCount);
            verify(storageService).getFilePath("unused-file1.jpg");
            verify(storageService).getFilePath("unused-file2.jpg");
            filesMock.verify(() -> Files.deleteIfExists(mockPath), times(1));
        }
    }

    @Test
    void cleanupUnusedFiles_WhenExceptionOccurs_ReturnsZero() {
        // given
        when(storageService.listAllFiles()).thenThrow(new RuntimeException("Storage error"));

        // when
        int deletedCount = imageCleanupService.cleanupUnusedFiles();

        // then
        assertEquals(0, deletedCount);
        verify(offerImageRepository, never()).findAll();
    }
}