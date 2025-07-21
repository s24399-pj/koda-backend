package pl.pjwstk.kodabackend.image.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.image.model.UploadStats;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageStatsServiceTest {

    @Mock
    private OfferImageRepository offerImageRepository;

    @Mock
    private FileStorageService storageService;

    @Mock
    private Path mockPath;

    @Mock
    private Path mockParentPath;

    @InjectMocks
    private ImageStatsService imageStatsService;

    @Test
    void getUploadStats_WhenEverythingWorks_ReturnsCorrectStats() throws IOException {
        // given
        when(storageService.listAllFiles()).thenReturn(Arrays.asList("file1.jpg", "file2.jpg", "file3.jpg"));
        when(storageService.getFilePath("file1.jpg")).thenReturn(mockPath);
        when(storageService.getFilePath("file2.jpg")).thenReturn(mockPath);
        when(storageService.getFilePath("file3.jpg")).thenReturn(mockPath);
        when(storageService.getFilePath("")).thenReturn(mockPath);
        when(mockPath.getParent()).thenReturn(mockParentPath);
        when(mockParentPath.toString()).thenReturn("/upload/directory");
        when(offerImageRepository.count()).thenReturn(5L);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.size(mockPath)).thenReturn(1000L, 2000L, 3000L);

            // when
            UploadStats stats = imageStatsService.getUploadStats();

            // then
            assertEquals(3, stats.totalFiles());
            assertEquals(6000L, stats.totalSizeBytes());
            assertEquals(5L, stats.totalImagesInDatabase());
            assertEquals("/upload/directory", stats.uploadDirectory());
        }
    }

    @Test
    void getUploadStats_WhenIOExceptionForSomeFiles_SkipsThemAndContinues() throws IOException {
        // given
        when(storageService.listAllFiles()).thenReturn(Arrays.asList("file1.jpg", "file2.jpg"));
        when(storageService.getFilePath("file1.jpg")).thenThrow(new IOException("Cannot access"));
        when(storageService.getFilePath("file2.jpg")).thenReturn(mockPath);
        when(storageService.getFilePath("")).thenReturn(mockPath);
        when(mockPath.getParent()).thenReturn(mockParentPath);
        when(mockParentPath.toString()).thenReturn("/upload/directory");
        when(offerImageRepository.count()).thenReturn(3L);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.size(mockPath)).thenReturn(5000L);

            // when
            UploadStats stats = imageStatsService.getUploadStats();

            // then
            assertEquals(2, stats.totalFiles());
            assertEquals(5000L, stats.totalSizeBytes());
            assertEquals(3L, stats.totalImagesInDatabase());
        }
    }

    @Test
    void getUploadStats_WhenExceptionOccurs_ReturnsDefaultStats() {
        // given
        when(storageService.listAllFiles()).thenThrow(new RuntimeException("Storage error"));

        // when
        UploadStats stats = imageStatsService.getUploadStats();

        // then
        assertEquals(0, stats.totalFiles());
        assertEquals(0L, stats.totalSizeBytes());
        assertEquals(0L, stats.totalImagesInDatabase());
        assertEquals("unknown", stats.uploadDirectory());
    }

    @Test
    void getUploadStats_WhenNoFiles_ReturnsZeroStats() throws IOException {
        // given
        when(storageService.listAllFiles()).thenReturn(Collections.emptyList());
        when(storageService.getFilePath("")).thenReturn(mockPath);
        when(mockPath.getParent()).thenReturn(mockParentPath);
        when(mockParentPath.toString()).thenReturn("/upload/directory");
        when(offerImageRepository.count()).thenReturn(0L);

        // when
        UploadStats stats = imageStatsService.getUploadStats();

        // then
        assertEquals(0, stats.totalFiles());
        assertEquals(0L, stats.totalSizeBytes());
        assertEquals(0L, stats.totalImagesInDatabase());
        assertEquals("/upload/directory", stats.uploadDirectory());
    }

    @Test
    void getUploadDirectory_WhenExceptionOccurs_ReturnsUnknown() throws IOException {
        // given
        when(storageService.listAllFiles()).thenReturn(Collections.emptyList());
        when(storageService.getFilePath("")).thenThrow(new IOException("Path error"));
        when(offerImageRepository.count()).thenReturn(0L);

        // when
        UploadStats stats = imageStatsService.getUploadStats();

        // then
        assertEquals("unknown", stats.uploadDirectory());
    }
}