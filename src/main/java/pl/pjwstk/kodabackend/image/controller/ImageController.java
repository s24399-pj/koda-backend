package pl.pjwstk.kodabackend.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.image.model.ImageUploadResponse;
import pl.pjwstk.kodabackend.image.model.UploadStats;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Image Management", description = "API for uploading, managing and deleting images (requires authentication)")
public interface ImageController {

    @Operation(
            summary = "Upload multiple images",
            description = "Uploads multiple images for a specific offer. Maximum 10 images, each up to 5MB."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully uploaded images",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file format or size",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer not found",
                    content = @Content
            )
    })
    @PostMapping(value = "/{offerId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<ImageUploadResponse> uploadImages(
            @Parameter(description = "Offer UUID")
            @PathVariable UUID offerId,
            @Parameter(description = "Image files to upload")
            @RequestParam("images") MultipartFile[] files,
            Principal principal
    );

    @Operation(
            summary = "Upload multiple images",
            description = "Uploads multiple images without specific offer. Maximum 10 images, each up to 5MB."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully uploaded images",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file format or size",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<ImageUploadResponse> uploadImages(
            @Parameter(description = "Image files to upload")
            @RequestParam("images") MultipartFile[] files,
            Principal principal
    );

    @Operation(
            summary = "Delete image",
            description = "Deletes an image by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted image"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image not found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @DeleteMapping("/{imageId}")
    void deleteImage(
            @Parameter(description = "Image UUID")
            @PathVariable UUID imageId,
            Principal principal
    );

    @Operation(
            summary = "Get upload statistics",
            description = "Retrieve statistics about uploaded files and images. Admin access required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistics retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UploadStats.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - admin access required"
            )
    })
    @GetMapping("/stats")
    UploadStats getUploadStats();

    @Operation(
            summary = "Cleanup unused files",
            description = "Remove files from disk that are not referenced in the database. Admin access required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cleanup completed successfully",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Deleted 5 unused files")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - admin access required"
            )
    })
    @PostMapping("/cleanup")
    ResponseEntity<String> cleanupUnusedFiles();
}
