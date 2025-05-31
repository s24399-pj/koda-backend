package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.image.config.ImageUploadProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileValidationService {

    private final ImageUploadProperties uploadProperties;

    public void validateUploadRequest(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BadRequestException("No files uploaded");
        }

        if (files.length > uploadProperties.getMaxFiles()) {
            throw new BadRequestException("You can upload maximum " + uploadProperties.getMaxFiles() + " files");
        }

        for (MultipartFile file : files) {
            validateFile(file);
        }

        log.debug("Validation of {} files completed successfully", files.length);
    }

    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > uploadProperties.getMaxFileSize()) {
            throw new BadRequestException("File is too large. Maximum size: " +
                    (uploadProperties.getMaxFileSize() / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !uploadProperties.getAllowedTypes().contains(contentType.toLowerCase())) {
            throw new BadRequestException("Unsupported file format. Allowed formats: JPG, PNG, WebP");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BadRequestException("Filename is required");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!uploadProperties.getAllowedExtensions().contains(extension)) {
            throw new BadRequestException("Unsupported file extension. Allowed: " +
                    uploadProperties.getAllowedExtensions());
        }

        log.debug("File validation completed successfully for {} (size: {} MB, type: {})",
                originalFilename, file.getSize() / 1024.0 / 1024.0, contentType);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}