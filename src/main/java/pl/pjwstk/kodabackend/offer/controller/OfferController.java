package pl.pjwstk.kodabackend.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.command.CreateOfferCommand;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Offers", description = "API for managing car offers")
public interface OfferController {

    @Operation(
            summary = "Get list of mini offers",
            description = "Returns a paginated list of offers in a condensed form. Filtering by phrase, minimum and maximum price is available."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of offers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid query parameters",
                    content = @Content
            )
    })
    @GetMapping
    Page<OfferMiniDto> findAllMini(
            @Parameter(description = "Pagination and sorting parameters") @PageableDefault Pageable pageable,
            @Parameter(description = "Search phrase (min 3, max 100 characters)")
            @RequestParam(required = false) @Size(min = 3, max = 100) String phrase,
            @Parameter(description = "Minimum price")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Owner/User ID to filter offers")
            @RequestParam(required = false) UUID userId
    );

    @Operation(
            summary = "Find offer names by phrase",
            description = "Returns a list of offer names matching the given phrase. Used for search suggestions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of offer names",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search phrase",
                    content = @Content
            )
    })
    @GetMapping("/find")
    List<String> findOfferNamesByPhrase(
            @Parameter(description = "Search phrase (min 3, max 100 characters)", required = true)
            @RequestParam @NotBlank @Size(min = 3, max = 100) String phrase
    );

    @Operation(
            summary = "Get offer details by ID",
            description = "Returns full offer details based on its UUID identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved offer details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer with the given ID was not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    OfferDto findOfferById(
            @Parameter(description = "Offer UUID identifier", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Create new car offer",
            description = "Creates a new car offer with detailed information. User must be authenticated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new offer",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid offer data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden, user does not have required permissions",
                    content = @Content
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OfferDto createOffer(
            @Parameter(description = "Offer creation data", required = true)
            @Valid @RequestBody CreateOfferCommand createOfferCommand,
            Principal principal
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    void deleteOffer(@PathVariable UUID id, Principal principal);
}