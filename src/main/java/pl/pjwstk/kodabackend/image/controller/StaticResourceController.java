package pl.pjwstk.kodabackend.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.image.service.StaticResourceService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@Tag(name = "Image Resources", description = "API for managing images and static resources")
class StaticResourceController {

    private final StaticResourceService staticResourceService;

    @Operation(
            summary = "View image",
            description = "Retrieves and displays an image with the specified filename"
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
    @GetMapping("/view/{filename}")
    ResponseEntity<Resource> viewImage(
            @Parameter(description = "Image filename", required = true)
            @PathVariable String filename) {

        log.debug("Image view request: {}", filename);
        return staticResourceService.serveImage(filename);
    }

    @Operation(
            summary = "List all images",
            description = "Retrieves a list of all available images in the system"
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
    @GetMapping("/list")
    ResponseEntity<List<String>> listImages() {
        log.debug("Image list request");
        return staticResourceService.listImages();
    }

    @Operation(
            summary = "Check image existence",
            description = "Checks if an image with the specified name exists in the system"
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
    @GetMapping("/exists/{filename}")
    ResponseEntity<Boolean> checkImageExists(
            @Parameter(description = "Image filename to check", required = true)
            @PathVariable String filename) {

        log.debug("Image existence check request: {}", filename);
        return staticResourceService.checkImageExists(filename);
    }
}