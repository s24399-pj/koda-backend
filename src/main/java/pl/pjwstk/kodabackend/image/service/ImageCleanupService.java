package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageCleanupService {

    private final OfferImageRepository offerImageRepository;
    private final FileStorageService storageService;

    @Transactional
    public int cleanupUnusedFiles() {
        log.info("Starting cleanup of unused files");

        try {
            List<String> allFiles = storageService.listAllFiles();
            List<String> usedFiles = offerImageRepository.findAll().stream()
                    .map(OfferImage::getUrl)
                    .map(url -> url.substring(url.lastIndexOf('/') + 1))
                    .toList();

            List<String> unusedFiles = allFiles.stream()
                    .filter(file -> !usedFiles.contains(file))
                    .toList();

            int deletedCount = 0;
            for (String unusedFile : unusedFiles) {
                try {
                    Path filePath = storageService.getFilePath(unusedFile);
                    Files.deleteIfExists(filePath);
                    deletedCount++;
                    log.debug("Deleted unused file: {}", unusedFile);
                } catch (IOException e) {
                    log.warn("Cannot delete file: {}", unusedFile, e);
                }
            }

            log.info("Cleanup completed. Deleted {} unused files out of {}", deletedCount, unusedFiles.size());
            return deletedCount;

        } catch (Exception e) {
            log.error("Error during file cleanup", e);
            return 0;
        }
    }
}