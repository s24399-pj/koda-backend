package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.image.model.UploadStats;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageStatsService {

    private final OfferImageRepository offerImageRepository;
    private final FileStorageService storageService;

    public UploadStats getUploadStats() {
        try {
            List<String> allFiles = storageService.listAllFiles();
            long totalSize = 0;

            for (String filename : allFiles) {
                try {
                    Path filePath = storageService.getFilePath(filename);
                    totalSize += Files.size(filePath);
                } catch (IOException e) {
                    log.warn("Cannot get file size: {}", filename);
                }
            }

            long totalImagesInDb = offerImageRepository.count();

            return UploadStats.builder()
                    .totalFiles(allFiles.size())
                    .totalSizeBytes(totalSize)
                    .totalImagesInDatabase(totalImagesInDb)
                    .uploadDirectory(getUploadDirectory())
                    .build();

        } catch (Exception e) {
            log.error("Error getting upload stats", e);
            return UploadStats.builder()
                    .totalFiles(0)
                    .totalSizeBytes(0)
                    .totalImagesInDatabase(0)
                    .uploadDirectory("unknown")
                    .build();
        }
    }

    private String getUploadDirectory() {
        try {
            return storageService.getFilePath("").getParent().toString();
        } catch (Exception e) {
            return "unknown";
        }
    }
}