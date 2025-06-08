package pl.pjwstk.kodabackend.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Brands", description = "API for retrieving car brands")
@RequestMapping("/api/v1/brands")
public interface BrandController {

    @Operation(
            summary = "Get all available car brands",
            description = "Returns a list of all unique car brands available in the database."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of brands",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            )
    })
    @GetMapping
    List<String> getAllBrands();

    @Operation(
            summary = "Find brands by phrase",
            description = "Returns a list of car brands matching the given phrase. Used for search suggestions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of matching brands",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            )
    })
    @GetMapping("/find")
    List<String> findBrandsByPhrase(
            @Parameter(description = "Search phrase (max 100 characters)")
            @RequestParam(required = false) @Size(max = 100) String phrase
    );
}