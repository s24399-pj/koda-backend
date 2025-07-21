package pl.pjwstk.kodabackend.image.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.image.config.ImageUploadProperties;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileValidationServiceTest {

    @Mock
    private ImageUploadProperties uploadProperties;

    private FileValidationService fileValidationService;

    @BeforeEach
    void setUp() {
        fileValidationService = new FileValidationService(uploadProperties);
    }

    @Test
    void validateUploadRequest_WhenValidFiles_PassesValidation() {
        // given
        when(uploadProperties.getMaxFiles()).thenReturn(5);
        when(uploadProperties.getMaxFileSize()).thenReturn(5 * 1024 * 1024L);
        when(uploadProperties.getAllowedTypes()).thenReturn(Set.of("image/jpeg", "image/png", "image/webp"));
        when(uploadProperties.getAllowedExtensions()).thenReturn(Set.of("jpg", "jpeg", "png", "webp"));

        MockMultipartFile file1 = new MockMultipartFile(
                "file", "test1.jpg", "image/jpeg", "content".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "test2.png", "image/png", "content".getBytes()
        );
        MultipartFile[] files = {file1, file2};

        // when & then
        assertDoesNotThrow(() -> fileValidationService.validateUploadRequest(files));
    }

    @Test
    void validateUploadRequest_WhenNullFiles_ThrowsBadRequestException() {
        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateUploadRequest(null)
        );
        assertEquals("No files uploaded", exception.getMessage());
    }

    @Test
    void validateUploadRequest_WhenEmptyArray_ThrowsBadRequestException() {
        // given
        MultipartFile[] files = new MultipartFile[0];

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateUploadRequest(files)
        );
        assertEquals("No files uploaded", exception.getMessage());
    }

    @Test
    void validateUploadRequest_WhenTooManyFiles_ThrowsBadRequestException() {
        // given
        when(uploadProperties.getMaxFiles()).thenReturn(5);

        MultipartFile[] files = new MultipartFile[6];
        for (int i = 0; i < 6; i++) {
            files[i] = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        }

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateUploadRequest(files)
        );
        assertEquals("You can upload maximum 5 files", exception.getMessage());
    }

    @Test
    void validateFile_WhenEmptyFile_ThrowsBadRequestException() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[0]
        );

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateFile(file)
        );
        assertEquals("File is empty", exception.getMessage());
    }

    @Test
    void validateFile_WhenFileTooLarge_ThrowsBadRequestException() {
        // given
        when(uploadProperties.getMaxFileSize()).thenReturn(5 * 1024 * 1024L);

        byte[] largeContent = new byte[6 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", largeContent
        );

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateFile(file)
        );
        assertEquals("File is too large. Maximum size: 5MB", exception.getMessage());
    }

    @Test
    void validateFile_WhenInvalidContentType_ThrowsBadRequestException() {
        // given
        when(uploadProperties.getMaxFileSize()).thenReturn(5 * 1024 * 1024L);
        when(uploadProperties.getAllowedTypes()).thenReturn(Set.of("image/jpeg", "image/png", "image/webp"));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "content".getBytes()
        );

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateFile(file)
        );
        assertEquals("Unsupported file format. Allowed formats: JPG, PNG, WebP", exception.getMessage());
    }

    @Test
    void validateFile_WhenNullFilename_ThrowsBadRequestException() {
        // given
        when(uploadProperties.getMaxFileSize()).thenReturn(5 * 1024 * 1024L);
        when(uploadProperties.getAllowedTypes()).thenReturn(Set.of("image/jpeg", "image/png", "image/webp"));

        MockMultipartFile file = new MockMultipartFile(
                "file", null, "image/jpeg", "content".getBytes()
        );

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateFile(file)
        );
        assertEquals("Filename is required", exception.getMessage());
    }

    @Test
    void validateFile_WhenInvalidExtension_ThrowsBadRequestException() {
        // given
        when(uploadProperties.getMaxFileSize()).thenReturn(5 * 1024 * 1024L);
        when(uploadProperties.getAllowedTypes()).thenReturn(Set.of("image/jpeg", "image/png", "image/webp"));
        when(uploadProperties.getAllowedExtensions()).thenReturn(Set.of("jpg", "jpeg", "png", "webp"));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.gif", "image/jpeg", "content".getBytes()
        );

        // when & then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> fileValidationService.validateFile(file)
        );
        assertTrue(exception.getMessage().startsWith("Unsupported file extension"));
    }
}