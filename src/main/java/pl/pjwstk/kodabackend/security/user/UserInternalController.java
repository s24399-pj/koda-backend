package pl.pjwstk.kodabackend.security.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pjwstk.kodabackend.security.user.model.AppUserDto;
import pl.pjwstk.kodabackend.security.user.model.UserMiniDto;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "User Internal Management", description = "Internal user management API")
public interface UserInternalController {

    @Operation(
            summary = "Get current user profile",
            description = "Returns the profile details of the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - User not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks required permissions",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    AppUserDto getUserDetails(@Parameter(hidden = true) Principal principal);

    @Operation(
            summary = "Get user profile by ID",
            description = "Returns the profile details of a specific user by their ID. Requires 'user:read' authority.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppUserDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - User not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks 'user:read' authority",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user ID format",
                    content = @Content
            )
    })
    ResponseEntity<AppUserDto> getUserProfileById(
            @Parameter(
                    description = "The UUID of the user to retrieve",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID userId
    );

    @Operation(
            summary = "Search users",
            description = "Search for users by name or email. Returns a list of matching users with minimal information. Requires 'user:read' authority.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved search results",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserMiniDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - User not authenticated",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks 'user:read' authority",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search query",
                    content = @Content
            )
    })
    ResponseEntity<List<UserMiniDto>> searchUsers(
            @Parameter(
                    description = "Search query to find users by name or email",
                    required = true,
                    example = "John Doe"
            )
            @RequestParam String query
    );
}