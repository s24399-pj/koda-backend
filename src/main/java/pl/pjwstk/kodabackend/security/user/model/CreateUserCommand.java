package pl.pjwstk.kodabackend.security.user.model;

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
public class CreateUserCommand {

    @NotBlank(message = "EMAIL_NOT_NULL")
    @Email(message = "EMAIL_NOT_VALID")
    @Size(min = 5, max = 150, message = "EMAIL_WRONG_SIZE")
    String email;

    @NotBlank(message = "PASSWORD_NOT_NULL")
    @Size(min = 5, max = 100, message = "PASSWORD_WRONG_SIZE")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$", message = "PASSWORD_REGEX_MISMATCH")
    String password;

    @Size(max = 50, message = "FIRST_NAME_TOO_LONG")
    @Pattern(regexp = "^[\\p{L} .'-]*$", message = "FIRST_NAME_INVALID_CHARACTERS")
    String firstName;

    @Size(max = 50, message = "LAST_NAME_TOO_LONG")
    @Pattern(regexp = "^[\\p{L} .'-]*$", message = "LAST_NAME_INVALID_CHARACTERS")
    String lastName;
}
