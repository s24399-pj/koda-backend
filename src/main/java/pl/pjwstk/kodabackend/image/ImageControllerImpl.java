package pl.pjwstk.kodabackend.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
class ImageControllerImpl implements ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/{offerId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Override
    public List<ImageUploadResponse> uploadImages(@PathVariable UUID offerId,
                                                  @RequestParam("images") MultipartFile[] files,
                                                  Principal principal) {
        String userEmail = principal.getName();
        log.info("Otrzymano żądanie uploadu {} plików dla oferty {} od użytkownika: {}",
                files.length, offerId, userEmail);
        return imageService.uploadImages(files, offerId, userEmail);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Override
    public List<ImageUploadResponse> uploadImages(@RequestParam("images") MultipartFile[] files,
                                                  Principal principal) {
        String userEmail = principal.getName();
        log.info("Otrzymano żądanie uploadu {} plików od użytkownika: {}", files.length, userEmail);
        return imageService.uploadImages(files, userEmail);
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deleteImage(@PathVariable UUID imageId, Principal principal) {
        String userEmail = principal.getName();
        log.info("Otrzymano żądanie usunięcia zdjęcia {} od użytkownika: {}", imageId, userEmail);
        imageService.deleteImage(imageId, userEmail);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ImageService.UploadStats getUploadStats() {
        return imageService.getUploadStats();
    }

    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cleanupUnusedFiles() {
        int deletedCount = imageService.cleanupUnusedFiles();
        return ResponseEntity.ok("Usunięto " + deletedCount + " nieużywanych plików");
    }
}