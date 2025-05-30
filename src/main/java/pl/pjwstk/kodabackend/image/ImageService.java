package pl.pjwstk.kodabackend.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.exception.NotFoundException;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final OfferImageRepository offerImageRepository;
    private final AppUserRepository appUserRepository;
    private final OfferRepository offerRepository;
    private final ResourceLoader resourceLoader;

    @Value("${koda.upload.dir:classpath:offer-images/}")
    private String uploadDir;

    @Value("${koda.upload.max-file-size:5242880}")
    private long maxFileSize;

    @Value("${koda.upload.max-files:10}")
    private int maxFiles;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    @Transactional
    public List<ImageUploadResponse> uploadImages(MultipartFile[] files, UUID offerId, String userEmail) {
        log.info("Rozpoczęcie uploadu {} plików dla oferty {} przez użytkownika: {}",
                files.length, offerId, userEmail);
        validateUploadRequest(files);

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Użytkownik nie został znaleziony"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Oferta nie została znaleziona"));

        if (!offer.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("Nie masz uprawnień do dodawania zdjęć do tej oferty");
        }

        int existingImagesCount = offerImageRepository.countByOfferId(offerId);
        int nextSortOrder = existingImagesCount + 1;

        List<ImageUploadResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                log.debug("Przetwarzanie pliku {}/{}: {}", i + 1, files.length, file.getOriginalFilename());

                String savedFilename = saveFile(file);
                String fileUrl = "/api/v1/images/view/" + savedFilename;

                boolean isFirstImageOverall = existingImagesCount == 0 && i == 0;

                OfferImage offerImage = OfferImage.builder()
                        .url(fileUrl)
                        .caption(file.getOriginalFilename())
                        .isPrimary(isFirstImageOverall)
                        .sortOrder(nextSortOrder + i)
                        .offer(offer)
                        .build();

                OfferImage saved = offerImageRepository.save(offerImage);

                responses.add(ImageUploadResponse.builder()
                        .id(saved.getId())
                        .url(saved.getUrl())
                        .filename(file.getOriginalFilename())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .sortOrder(saved.getSortOrder())
                        .build());

                log.info("Zapisano zdjęcie: {} (ID: {}) dla oferty: {} użytkownika: {}",
                        savedFilename, saved.getId(), offerId, userEmail);

            } catch (IOException e) {
                log.error("Błąd podczas zapisywania pliku: {} - {}", file.getOriginalFilename(), e.getMessage());
                throw new BadRequestException("Błąd podczas zapisywania pliku: " + file.getOriginalFilename());
            }
        }

        log.info("Zakończono upload {} plików dla oferty {}. Utworzono {} rekordów.",
                files.length, offerId, responses.size());
        return responses;
    }

    @Transactional
    public List<ImageUploadResponse> uploadImages(MultipartFile[] files, String userEmail) {
        log.info("Rozpoczęcie uploadu {} plików przez użytkownika: {}", files.length, userEmail);
        validateUploadRequest(files);

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Użytkownik nie został znaleziony"));

        List<ImageUploadResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                log.debug("Przetwarzanie pliku {}/{}: {}", i + 1, files.length, file.getOriginalFilename());

                String savedFilename = saveFile(file);
                String fileUrl = "/api/v1/images/view/" + savedFilename;

                OfferImage offerImage = OfferImage.builder()
                        .url(fileUrl)
                        .caption(file.getOriginalFilename())
                        .isPrimary(i == 0)
                        .sortOrder(i + 1)
                        .build();

                OfferImage saved = offerImageRepository.save(offerImage);

                responses.add(ImageUploadResponse.builder()
                        .id(saved.getId())
                        .url(saved.getUrl())
                        .filename(file.getOriginalFilename())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .sortOrder(saved.getSortOrder())
                        .build());

                log.info("Zapisano zdjęcie: {} (ID: {}) dla użytkownika: {}",
                        savedFilename, saved.getId(), userEmail);

            } catch (IOException e) {
                log.error("Błąd podczas zapisywania pliku: {} - {}", file.getOriginalFilename(), e.getMessage());
                throw new BadRequestException("Błąd podczas zapisywania pliku: " + file.getOriginalFilename());
            }
        }

        log.info("Zakończono upload {} plików. Utworzono {} rekordów.", files.length, responses.size());
        return responses;
    }

    @Transactional
    public void deleteImage(UUID imageId, String userEmail) {
        log.info("Usuwanie zdjęcia {} przez użytkownika: {}", imageId, userEmail);

        OfferImage image = offerImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Zdjęcie nie zostało znalezione"));

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Użytkownik nie został znaleziony"));

        if (image.getOffer() != null && !image.getOffer().getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("Nie masz uprawnień do usunięcia tego zdjęcia");
        }

        try {
            deleteFileFromDisk(image.getUrl());

            offerImageRepository.delete(image);

            log.info("Usunięto zdjęcie: {} przez użytkownika: {}", imageId, userEmail);
        } catch (IOException e) {
            log.error("Błąd podczas usuwania pliku: {}", e.getMessage());
            throw new BadRequestException("Błąd podczas usuwania pliku");
        }
    }

    private void validateUploadRequest(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BadRequestException("Nie przesłano żadnych plików");
        }

        if (files.length > maxFiles) {
            throw new BadRequestException("Można przesłać maksymalnie " + maxFiles + " plików");
        }

        for (MultipartFile file : files) {
            validateFile(file);
        }

        log.debug("Walidacja {} plików zakończona pomyślnie", files.length);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Plik jest pusty");
        }

        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("Plik jest za duży. Maksymalny rozmiar: " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Nieobsługiwany format pliku. Dozwolone formaty: JPG, PNG, WebP");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BadRequestException("Nazwa pliku jest wymagana");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Nieobsługiwane rozszerzenie pliku. Dozwolone: " + ALLOWED_EXTENSIONS);
        }

        log.debug("Walidacja pliku {} zakończona pomyślnie (rozmiar: {} MB, typ: {})",
                originalFilename, file.getSize() / 1024.0 / 1024.0, contentType);
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = getUploadPath();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Utworzono katalog: {}", uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

        Path filePath = uploadPath.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Zapisano plik: {} w lokalizacji: {} (rozmiar: {} bajtów)",
                uniqueFilename, filePath, file.getSize());
        return uniqueFilename;
    }

    private void deleteFileFromDisk(String fileUrl) throws IOException {
        String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        log.debug("Usuwanie pliku z dysku: {}", filename);

        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Usunięto plik: {}", filePath);
        } else {
            log.warn("Plik {} nie istnieje w lokalizacji: {}", filename, filePath);
        }
    }

    private Path getUploadPath() throws IOException {
        if (uploadDir.startsWith("classpath:")) {
            String resourcePath = uploadDir.replace("classpath:", "");

            try {
                Resource resource = resourceLoader.getResource(uploadDir);
                if (resource.exists()) {
                    Path path = Paths.get(resource.getURI());
                    log.debug("Użyto ścieżki z resources: {}", path);
                    return path;
                }
            } catch (Exception e) {
                log.debug("Nie można pobrać ścieżki z resources: {}, używam bezpośredniej ścieżki", e.getMessage());
            }

            String projectPath = System.getProperty("user.dir");
            Path fallbackPath = Paths.get(projectPath, "src", "main", "resources", resourcePath);
            log.debug("Użyto fallback ścieżki: {}", fallbackPath);
            return fallbackPath;
        } else {
            Path systemPath = Paths.get(uploadDir);
            log.debug("Użyto ścieżki systemowej: {}", systemPath);
            return systemPath;
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }

    public boolean fileExists(String filename) {
        try {
            Path uploadPath = getUploadPath();
            Path filePath = uploadPath.resolve(filename);
            boolean exists = Files.exists(filePath);
            log.debug("Sprawdzanie istnienia pliku {}: {}", filename, exists);
            return exists;
        } catch (IOException e) {
            log.error("Błąd podczas sprawdzania istnienia pliku: {}", filename, e);
            return false;
        }
    }

    public List<String> listAllFiles() {
        try {
            Path uploadPath = getUploadPath();
            if (!Files.exists(uploadPath)) {
                log.warn("Katalog {} nie istnieje", uploadPath);
                return new ArrayList<>();
            }

            List<String> files = Files.list(uploadPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();

            log.debug("Znaleziono {} plików w katalogu {}", files.size(), uploadPath);
            return files;
        } catch (IOException e) {
            log.error("Błąd podczas listowania plików", e);
            return new ArrayList<>();
        }
    }

    public Path getFilePath(String filename) throws IOException {
        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename);
        log.debug("Pobrano ścieżkę do pliku {}: {}", filename, filePath);
        return filePath;
    }

    public Optional<FileInfo> getFileInfo(String filename) {
        try {
            Path filePath = getFilePath(filename);
            if (!Files.exists(filePath)) {
                return Optional.empty();
            }

            FileInfo info = FileInfo.builder()
                    .filename(filename)
                    .size(Files.size(filePath))
                    .lastModified(Files.getLastModifiedTime(filePath).toInstant())
                    .contentType(Files.probeContentType(filePath))
                    .build();

            return Optional.of(info);
        } catch (IOException e) {
            log.error("Błąd podczas pobierania informacji o pliku: {}", filename, e);
            return Optional.empty();
        }
    }

    @Transactional
    public int cleanupUnusedFiles() {
        log.info("Rozpoczęcie czyszczenia nieużywanych plików");

        try {
            List<String> allFiles = listAllFiles();
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
                    Path filePath = getFilePath(unusedFile);
                    Files.deleteIfExists(filePath);
                    deletedCount++;
                    log.debug("Usunięto nieużywany plik: {}", unusedFile);
                } catch (IOException e) {
                    log.warn("Nie można usunąć pliku: {}", unusedFile, e);
                }
            }

            log.info("Czyszczenie zakończone. Usunięto {} nieużywanych plików z {}", deletedCount, unusedFiles.size());
            return deletedCount;

        } catch (Exception e) {
            log.error("Błąd podczas czyszczenia plików", e);
            return 0;
        }
    }

    public UploadStats getUploadStats() {
        try {
            List<String> allFiles = listAllFiles();
            long totalSize = 0;

            for (String filename : allFiles) {
                try {
                    Path filePath = getFilePath(filename);
                    totalSize += Files.size(filePath);
                } catch (IOException e) {
                    log.warn("Nie można pobrać rozmiaru pliku: {}", filename);
                }
            }

            long totalImagesInDb = offerImageRepository.count();

            return UploadStats.builder()
                    .totalFiles(allFiles.size())
                    .totalSizeBytes(totalSize)
                    .totalImagesInDatabase(totalImagesInDb)
                    .uploadDirectory(getUploadPath().toString())
                    .build();

        } catch (Exception e) {
            log.error("Błąd podczas pobierania statystyk", e);
            return UploadStats.builder()
                    .totalFiles(0)
                    .totalSizeBytes(0)
                    .totalImagesInDatabase(0)
                    .uploadDirectory("unknown")
                    .build();
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class FileInfo {
        private String filename;
        private long size;
        private Instant lastModified;
        private String contentType;
    }

    @lombok.Data
    @lombok.Builder
    public static class UploadStats {
        private int totalFiles;
        private long totalSizeBytes;
        private long totalImagesInDatabase;
        private String uploadDirectory;

        public String getTotalSizeFormatted() {
            if (totalSizeBytes < 1024) return totalSizeBytes + " B";
            if (totalSizeBytes < 1024 * 1024) return String.format("%.1f KB", totalSizeBytes / 1024.0);
            if (totalSizeBytes < 1024 * 1024 * 1024)
                return String.format("%.1f MB", totalSizeBytes / (1024.0 * 1024.0));
            return String.format("%.1f GB", totalSizeBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}