package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.image.model.ImageUploadResponse;
import pl.pjwstk.kodabackend.image.model.UploadStats;
import pl.pjwstk.kodabackend.image.usecase.DeleteImageUseCase;
import pl.pjwstk.kodabackend.image.usecase.ImageUploadUseCase;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUploadUseCase uploadUseCase;
    private final DeleteImageUseCase deleteUseCase;
    private final FileStorageService storageService;
    private final ImageCleanupService cleanupService;
    private final ImageStatsService statsService;

    public List<ImageUploadResponse> uploadImages(MultipartFile[] files, UUID offerId, String userEmail) {
        return uploadUseCase.uploadImagesForOffer(files, offerId, userEmail);
    }

    public List<ImageUploadResponse> uploadImages(MultipartFile[] files, String userEmail) {
        return uploadUseCase.uploadStandaloneImages(files, userEmail);
    }

    public void deleteImage(UUID imageId, String userEmail) {
        deleteUseCase.deleteImage(imageId, userEmail);
    }

    public boolean fileExists(String filename) {
        return storageService.fileExists(filename);
    }

    public List<String> listAllFiles() {
        return storageService.listAllFiles();
    }

    public int cleanupUnusedFiles() {
        return cleanupService.cleanupUnusedFiles();
    }

    public UploadStats getUploadStats() {
        return statsService.getUploadStats();
    }
}