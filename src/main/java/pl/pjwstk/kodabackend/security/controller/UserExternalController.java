package pl.pjwstk.kodabackend.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.pjwstk.kodabackend.security.user.model.AuthenticationResponse;
import pl.pjwstk.kodabackend.security.user.model.CreateUserCommand;
import pl.pjwstk.kodabackend.security.user.model.LoginRequest;

@Tag(name = "User Management", description = "External user management API")
public interface UserExternalController {

    @Operation(
            summary = "Register a new user",
            description = "Endpoint for registering a new external user in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = Error.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with given email/username already exists",
                    content = @Content(schema = @Schema(implementation = Error.class))
            )
    })
    @PostMapping("/register")
    void register(@Valid @RequestBody CreateUserCommand command);

    @Operation(
            summary = "Login user",
            description = "Endpoint for authenticating a user and obtaining a JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Error.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = Error.class))
            )
    })
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest);
}