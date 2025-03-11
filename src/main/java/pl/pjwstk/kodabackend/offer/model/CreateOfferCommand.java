package pl.pjwstk.kodabackend.offer.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//todo this is template need to be implemented
public record CreateOfferCommand(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        @Valid
        @NotNull(message = "Car details are required")
        CarEquipmentDto carDetails,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        BigDecimal price,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
        String contactPhone,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Contact email is required")
        String contactEmail,

        @FutureOrPresent(message = "Expiration date must be in the future or present")
        LocalDateTime expirationDate,

        boolean featured,

        boolean negotiable
) {
}
