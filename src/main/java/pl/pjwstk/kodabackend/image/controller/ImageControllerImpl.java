package pl.pjwstk.kodabackend.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.image.model.ImageUploadResponse;
import pl.pjwstk.kodabackend.image.model.UploadStats;
import pl.pjwstk.kodabackend.image.service.ImageService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
        return imageService.uploadImages(files, offerId, principal.getName());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Override
    public List<ImageUploadResponse> uploadImages(@RequestParam("images") MultipartFile[] files,
                                                  Principal principal) {
        return imageService.uploadImages(files, principal.getName());
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deleteImage(@PathVariable UUID imageId, Principal principal) {
        imageService.deleteImage(imageId, principal.getName());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UploadStats getUploadStats() {
        return imageService.getUploadStats();
    }

    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<String> cleanupUnusedFiles() {
        int deletedCount = imageService.cleanupUnusedFiles();
        return ResponseEntity.ok("Deleted " + deletedCount + " unused files");
    }
}