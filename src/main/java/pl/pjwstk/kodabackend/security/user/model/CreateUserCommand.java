package pl.pjwstk.kodabackend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Command for creating a new user account")
public class CreateUserCommand {

    @Schema(description = "User's email address (must be valid and between 5-150 characters)",
            example = "john.doe@example.com")
    @NotBlank(message = "EMAIL_NOT_NULL")
    @Email(message = "EMAIL_NOT_VALID")
    @Size(min = 5, max = 150, message = "EMAIL_WRONG_SIZE")
    String email;

    @Schema(description = "User's password (minimum 8 characters, must contain at least one digit and one uppercase letter)",
            example = "SecurePass123",
            minLength = 8,
            maxLength = 100)
    @NotBlank(message = "PASSWORD_NOT_NULL")
    @Size(min = 5, max = 100, message = "PASSWORD_WRONG_SIZE")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$", message = "PASSWORD_REGEX_MISMATCH")
    String password;

    @Schema(description = "User's first name (optional, max 50 characters, letters, spaces, dots, apostrophes and hyphens only)",
            example = "John",
            maxLength = 50)
    @Size(max = 50, message = "FIRST_NAME_TOO_LONG")
    @Pattern(regexp = "^[\\p{L} .'-]*$", message = "FIRST_NAME_INVALID_CHARACTERS")
    String firstName;

    @Schema(description = "User's last name (optional, max 50 characters, letters, spaces, dots, apostrophes and hyphens only)",
            example = "Doe",
            maxLength = 50)
    @Size(max = 50, message = "LAST_NAME_TOO_LONG")
    @Pattern(regexp = "^[\\p{L} .'-]*$", message = "LAST_NAME_INVALID_CHARACTERS")
    String lastName;

    @Schema(description = "User's phone number",
            example = "123456789",
            minLength = 9,
            maxLength = 9)
    @Size(min = 9, max = 9, message = "PHONE_NUMBER_INVALID_LENGTH")
    @Pattern(regexp = "^[0-9]{9}$", message = "PHONE_NUMBER_INVALID_FORMAT")
    String phoneNumber;
}