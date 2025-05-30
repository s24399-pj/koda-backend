package pl.pjwstk.kodabackend.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class StaticResourceController {

    private final ResourceLoader resourceLoader;
    private final ImageService imageService;

    @Value("${koda.upload.dir:classpath:offer-images/}")
    private String uploadDir;

    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewImage(@PathVariable String filename) {
        try {
            log.debug("Żądanie wyświetlenia zdjęcia: {}", filename);

            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                log.warn("Niebezpieczna nazwa pliku: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            Resource resource;
            String contentType;

            if (uploadDir.startsWith("classpath:")) {
                String resourcePath = uploadDir + filename;
                resource = resourceLoader.getResource(resourcePath);

                if (!resource.exists()) {
                    log.warn("Plik nie został znaleziony w resources: {}", resourcePath);
                    return ResponseEntity.notFound().build();
                }

                contentType = Files.probeContentType(Paths.get(resource.getFilename()));
            } else {
                Path filePath = Paths.get(uploadDir, filename);

                if (!Files.exists(filePath)) {
                    log.warn("Plik nie został znaleziony: {}", filePath);
                    return ResponseEntity.notFound().build();
                }

                resource = new FileSystemResource(filePath);
                contentType = Files.probeContentType(filePath);
            }

            if (contentType == null) {
                contentType = getContentTypeByExtension(filename);
            }

            log.debug("Serwowanie pliku: {} z typem: {}", filename, contentType);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Błąd podczas serwowania pliku: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<java.util.List<String>> listImages() {
        try {
            java.util.List<String> files = imageService.listAllFiles();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("Błąd podczas listowania plików", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exists/{filename}")
    public ResponseEntity<Boolean> checkImageExists(@PathVariable String filename) {
        try {
            boolean exists = imageService.fileExists(filename);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Błąd podczas sprawdzania istnienia pliku: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    private String getContentTypeByExtension(String filename) {
        if (filename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String extension = filename.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (extension.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (extension.endsWith(".webp")) {
            return "image/webp";
        } else if (extension.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        }

        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}