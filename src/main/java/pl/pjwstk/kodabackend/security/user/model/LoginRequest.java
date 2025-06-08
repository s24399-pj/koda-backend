package pl.pjwstk.kodabackend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@Schema(description = "User login request containing email and password")
public class LoginRequest {

    @Schema(description = "User's email address (must be valid and between 5-150 characters)",
            example = "john.doe@example.com")
    @NotBlank(message = "EMAIL_NOT_NULL")
    @Email(message = "EMAIL_NOT_VALID")
    @Size(min = 5, max = 150, message = "EMAIL_WRONG_SIZE")
    String email;

    @Schema(description = "User's password (minimum 5 characters)",
            example = "mySecurePassword123",
            minLength = 5,
            maxLength = 100)
    @NotBlank(message = "PASSWORD_NOT_NULL")
    @Size(min = 5, max = 100, message = "PASSWORD_WRONG_SIZE")
    String password;
}