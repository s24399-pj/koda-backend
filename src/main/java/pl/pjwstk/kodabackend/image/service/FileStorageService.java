package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.image.config.ImageUploadProperties;
import pl.pjwstk.kodabackend.image.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ImageUploadProperties.class)
public class FileStorageService {

    private final ImageUploadProperties uploadProperties;
    private final ResourceLoader resourceLoader;

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = getUploadPath();
        ensureDirectoryExists(uploadPath);

        String uniqueFilename = generateUniqueFilename(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(uniqueFilename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saved file: {} (size: {} bytes)", uniqueFilename, file.getSize());

        return uniqueFilename;
    }

    public void deleteFile(String fileUrl) throws IOException {
        String filename = extractFilename(fileUrl);
        Path filePath = getUploadPath().resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.debug("Deleted file: {}", filename);
        } else {
            log.warn("File not found for deletion: {}", filename);
        }
    }

    public boolean fileExists(String filename) {
        try {
            Path filePath = getUploadPath().resolve(filename);
            return Files.exists(filePath);
        } catch (IOException e) {
            log.error("Error checking file existence: {}", filename, e);
            return false;
        }
    }

    public List<String> listAllFiles() {
        try {
            Path uploadPath = getUploadPath();
            if (!Files.exists(uploadPath)) {
                log.warn("Upload directory does not exist: {}", uploadPath);
                return List.of();
            }

            return Files.list(uploadPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();
        } catch (IOException e) {
            log.error("Error listing files", e);
            return List.of();
        }
    }

    public Path getFilePath(String filename) throws IOException {
        return getUploadPath().resolve(filename);
    }

    public Optional<FileInfo> getFileInfo(String filename) {
        try {
            Path filePath = getFilePath(filename);
            if (!Files.exists(filePath)) {
                return Optional.empty();
            }

            return Optional.of(FileInfo.builder()
                    .filename(filename)
                    .size(Files.size(filePath))
                    .lastModified(Files.getLastModifiedTime(filePath).toInstant())
                    .contentType(Files.probeContentType(filePath))
                    .build());
        } catch (IOException e) {
            log.error("Error getting file info for: {}", filename, e);
            return Optional.empty();
        }
    }

    private void ensureDirectoryExists(Path uploadPath) throws IOException {
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created upload directory: {}", uploadPath);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID() + "." + extension;
    }

    private String extractFilename(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }

    private Path getUploadPath() throws IOException {
        if (uploadProperties.getDir().startsWith("classpath:")) {
            return resolveClasspathPath();
        } else {
            return Paths.get(uploadProperties.getDir());
        }
    }

    private Path resolveClasspathPath() {
        String resourcePath = uploadProperties.getDir().replace("classpath:", "");

        try {
            Resource resource = resourceLoader.getResource(uploadProperties.getDir());
            if (resource.exists()) {
                return Paths.get(resource.getURI());
            }
        } catch (Exception e) {
            log.debug("Cannot resolve classpath resource, using fallback path");
        }

        // Fallback to project directory structure
        String projectPath = System.getProperty("user.dir");
        return Paths.get(projectPath, "src", "main", "resources", resourcePath);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}