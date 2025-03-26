package pl.pjwstk.kodabackend.security.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "EMAIL_NOT_NULL")
    @Email(message = "EMAIL_NOT_VALID")
    @Size(min = 5, max = 150, message = "EMAIL_WRONG_SIZE")
    String email;

    @NotBlank(message = "PASSWORD_NOT_NULL")
    @Size(min = 5, max = 100, message = "PASSWORD_WRONG_SIZE")
    String password;
}
