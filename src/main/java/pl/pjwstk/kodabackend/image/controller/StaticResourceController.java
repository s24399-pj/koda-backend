package pl.pjwstk.kodabackend.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Image Viewing", description = "API for viewing and browsing images (public access, no authentication required)")
public interface StaticResourceController {

    @Operation(
            summary = "View image",
            description = "Retrieve and display an image with the specified filename. Public access, no authentication required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image successfully retrieved",
                    content = @Content(mediaType = "image/*")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filename"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/images/{filename}")
    ResponseEntity<Resource> viewImage(
            @Parameter(description = "Image filename", required = true)
            @PathVariable String filename);

    @Operation(
            summary = "List all images",
            description = "Retrieve a list of all available images in the system. Public access, no authentication required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image list successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while retrieving file list"
            )
    })
    @GetMapping("/images")
    ResponseEntity<List<String>> listImages();

    @Operation(
            summary = "Check image existence",
            description = "Check if an image with the specified name exists in the system. Public access, no authentication required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Check completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "boolean")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while checking file"
            )
    })
    @GetMapping("/images/exists/{filename}")
    ResponseEntity<Boolean> checkImageExists(
            @Parameter(description = "Image filename to check", required = true)
            @PathVariable String filename);
}