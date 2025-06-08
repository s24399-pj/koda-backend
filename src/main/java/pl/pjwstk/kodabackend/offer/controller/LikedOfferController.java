package pl.pjwstk.kodabackend.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pjwstk.kodabackend.offer.model.OfferDto;

import java.util.List;
import java.util.UUID;

@Tag(name = "Favorites", description = "API for managing user favorite offers")
public interface LikedOfferController {

    @Operation(
            summary = "Get user's liked offers",
            description = "Retrieves all offers that the authenticated user has marked as favorites/liked."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user's liked offers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            )
    })
    @GetMapping("/liked")
    List<OfferDto> getLikedOffers(Authentication authentication);

    @Operation(
            summary = "Check if offer is liked by user",
            description = "Checks whether the specified offer is marked as favorite by the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully checked offer like status",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer with the given ID was not found",
                    content = @Content
            )
    })
    @GetMapping("/{offerId}/liked")
    Boolean isOfferLiked(
            @Parameter(description = "Offer UUID identifier", required = true)
            @PathVariable UUID offerId,
            Authentication authentication
    );

    @Operation(
            summary = "Like an offer",
            description = "Adds the specified offer to user's favorites/liked offers list."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully liked the offer"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Offer is already liked by user"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer with the given ID was not found",
                    content = @Content
            )
    })
    @PostMapping("/{offerId}/like")
    void likeOffer(
            @Parameter(description = "Offer UUID identifier", required = true)
            @PathVariable UUID offerId,
            Authentication authentication
    );

    @Operation(
            summary = "Unlike an offer",
            description = "Removes the specified offer from user's favorites/liked offers list."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully unliked the offer"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Offer is not liked by user"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer with the given ID was not found",
                    content = @Content
            )
    })
    @PostMapping("/{offerId}/unlike")
    void unlikeOffer(
            @Parameter(description = "Offer UUID identifier", required = true)
            @PathVariable UUID offerId,
            Authentication authentication
    );

    @Operation(
            summary = "Toggle offer like status",
            description = "Toggles the like status of an offer - if liked, it will be unliked; if not liked, it will be liked."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully toggled offer like status"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Offer with the given ID was not found",
                    content = @Content
            )
    })
    @PostMapping("/{offerId}/toggle")
    void toggleLikedOffer(
            @Parameter(description = "Offer UUID identifier", required = true)
            @PathVariable UUID offerId,
            Authentication authentication
    );
}