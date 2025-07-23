package pl.pjwstk.kodabackend.image.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import pl.pjwstk.kodabackend.image.config.ImageUploadProperties;
import pl.pjwstk.kodabackend.image.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @TempDir
    Path tempDir;
    @Mock
    private ImageUploadProperties uploadProperties;
    @Mock
    private ResourceLoader resourceLoader;
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(uploadProperties, resourceLoader);
    }

    @Test
    void saveFile_WhenValidFile_SavesSuccessfully() throws IOException {
        // given
        when(uploadProperties.getDir()).thenReturn(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test content".getBytes()
        );

        // when
        String savedFilename = fileStorageService.saveFile(file);

        // then
        assertNotNull(savedFilename);
        assertTrue(savedFilename.endsWith(".jpg"));
        Path savedFile = tempDir.resolve(savedFilename);
        assertTrue(Files.exists(savedFile));
        assertEquals("test content", Files.readString(savedFile));
    }

    @Test
    void saveFile_WhenNoExtension_SavesWithoutExtension() throws IOException {
        // given
        when(uploadProperties.getDir()).thenReturn(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file", "testfile", "text/plain", "content".getBytes()
        );

        // when
        String savedFilename = fileStorageService.saveFile(file);

        // then
        assertNotNull(savedFilename);
        assertTrue(savedFilename.endsWith("."));
        assertTrue(Files.exists(tempDir.resolve(savedFilename)));
    }

    @Test
    void deleteFile_WhenFileExists_DeletesSuccessfully() throws IOException {
        // given
        when(uploadProperties.getDir()).thenReturn(tempDir.toString());
        Path testFile = tempDir.resolve("test-file.jpg");
        Files.writeString(testFile, "test content");
        String fileUrl = "/api/v1/images/view/test-file.jpg";

        // when
        fileStorageService.deleteFile(fileUrl);

        // then
        assertFalse(Files.exists(testFile));
    }

    @Test
    void deleteFile_WhenFileDoesNotExist_DoesNotThrow() {
        // given
        when(uploadProperties.getDir()).thenReturn(tempDir.toString());
        String fileUrl = "/api/v1/images/view/non-existent.jpg";

        // when & then
        assertDoesNotThrow(() -> fileStorageService.deleteFile(fileUrl));
    }

    @Test
    void getFileInfo_WhenFileDoesNotExist_ReturnsEmpty() {
        // given
        when(uploadProperties.getDir()).thenReturn(tempDir.toString());

        // when
        Optional<FileInfo> fileInfo = fileStorageService.getFileInfo("non-existent.jpg");

        // then
        assertTrue(fileInfo.isEmpty());
    }
}