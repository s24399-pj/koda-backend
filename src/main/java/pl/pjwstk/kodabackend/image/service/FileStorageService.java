package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.image.config.ImageUploadProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final ImageUploadProperties uploadProperties;
    private final ResourceLoader resourceLoader;

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = getUploadPath();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created directory: {}", uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + "." + extension;

        Path filePath = uploadPath.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved file: {} at location: {} (size: {} bytes)",
                uniqueFilename, filePath, file.getSize());
        return uniqueFilename;
    }

    public void deleteFile(String fileUrl) throws IOException {
        String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        log.debug("Deleting file from disk: {}", filename);

        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Deleted file: {}", filePath);
        } else {
            log.warn("File {} does not exist at location: {}", filename, filePath);
        }
    }

    public boolean fileExists(String filename) {
        try {
            Path uploadPath = getUploadPath();
            Path filePath = uploadPath.resolve(filename);
            boolean exists = Files.exists(filePath);
            log.debug("Checking file existence {}: {}", filename, exists);
            return exists;
        } catch (IOException e) {
            log.error("Error checking file existence: {}", filename, e);
            return false;
        }
    }

    public List<String> listAllFiles() {
        try {
            Path uploadPath = getUploadPath();
            if (!Files.exists(uploadPath)) {
                log.warn("Directory {} does not exist", uploadPath);
                return List.of();
            }

            List<String> files = Files.list(uploadPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();

            log.debug("Found {} files in directory {}", files.size(), uploadPath);
            return files;
        } catch (IOException e) {
            log.error("Error listing files", e);
            return List.of();
        }
    }

    public Path getFilePath(String filename) throws IOException {
        Path uploadPath = getUploadPath();
        Path filePath = uploadPath.resolve(filename);
        log.debug("Retrieved file path for {}: {}", filename, filePath);
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
            log.error("Error getting file info: {}", filename, e);
            return Optional.empty();
        }
    }

    private Path getUploadPath() throws IOException {
        if (uploadProperties.getDir().startsWith("classpath:")) {
            String resourcePath = uploadProperties.getDir().replace("classpath:", "");

            try {
                Resource resource = resourceLoader.getResource(uploadProperties.getDir());
                if (resource.exists()) {
                    Path path = Paths.get(resource.getURI());
                    log.debug("Using resources path: {}", path);
                    return path;
                }
            } catch (Exception e) {
                log.debug("Cannot get path from resources: {}, using direct path", e.getMessage());
            }

            String projectPath = System.getProperty("user.dir");
            Path fallbackPath = Paths.get(projectPath, "src", "main", "resources", resourcePath);
            log.debug("Using fallback path: {}", fallbackPath);
            return fallbackPath;
        } else {
            Path systemPath = Paths.get(uploadProperties.getDir());
            log.debug("Using system path: {}", systemPath);
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

    public record FileInfo(
            String filename,
            long size,
            Instant lastModified,
            String contentType
    ) {
        public static FileInfoBuilder builder() {
            return new FileInfoBuilder();
        }

        public static class FileInfoBuilder {
            private String filename;
            private long size;
            private Instant lastModified;
            private String contentType;

            public FileInfoBuilder filename(String filename) {
                this.filename = filename;
                return this;
            }

            public FileInfoBuilder size(long size) {
                this.size = size;
                return this;
            }

            public FileInfoBuilder lastModified(Instant lastModified) {
                this.lastModified = lastModified;
                return this;
            }

            public FileInfoBuilder contentType(String contentType) {
                this.contentType = contentType;
                return this;
            }

            public FileInfo build() {
                return new FileInfo(filename, size, lastModified, contentType);
            }
        }
    }
}