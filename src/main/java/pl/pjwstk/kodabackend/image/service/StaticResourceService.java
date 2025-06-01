package pl.pjwstk.kodabackend.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.image.strategy.ResourceLocationStrategy;
import pl.pjwstk.kodabackend.image.builder.ImageResponseBuilder;
import pl.pjwstk.kodabackend.image.resolver.ContentTypeResolver;
import pl.pjwstk.kodabackend.image.strategy.ResourceLocation;
import pl.pjwstk.kodabackend.image.validator.FilenameValidator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaticResourceService {

    private final ImageService imageService;
    private final ResourceLocationStrategy resourceLocationStrategy;
    private final FilenameValidator filenameValidator;
    private final ContentTypeResolver contentTypeResolver;
    private final ImageResponseBuilder responseBuilder;

    public ResponseEntity<Resource> serveImage(String filename) {
        try {
            if (!filenameValidator.isValid(filename)) {
                log.warn("Dangerous filename detected: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            ResourceLocation location = resourceLocationStrategy.resolveLocation(filename);
            if (!location.exists()) {
                log.warn("File not found: {}", filename);
                return ResponseEntity.notFound().build();
            }

            String contentType = contentTypeResolver.resolveContentType(filename, location);
            log.debug("Serving file: {} with type: {}", filename, contentType);

            return responseBuilder.buildResponse(location.resource(), contentType, filename);

        } catch (Exception e) {
            log.error("Error serving file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<String>> listImages() {
        try {
            List<String> files = imageService.listAllFiles();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("Error listing files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Boolean> checkImageExists(String filename) {
        try {
            boolean exists = imageService.fileExists(filename);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking file existence: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}