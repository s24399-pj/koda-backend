package pl.pjwstk.kodabackend.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Images", description = "API for managing offer images")
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
            @Parameter(description = "Offer UUID", required = true)
            @PathVariable UUID offerId,
            @Parameter(description = "Image files to upload", required = true)
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
            @Parameter(description = "Image files to upload", required = true)
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
            @Parameter(description = "Image UUID", required = true)
            @PathVariable UUID imageId,
            Principal principal
    );
}