package pl.pjwstk.kodabackend.offer.model.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pjwstk.kodabackend.offer.persistence.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistence.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistence.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistence.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistence.entity.VehicleCondition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command for creating a new vehicle offer")
public class CreateOfferCommand {

    @Schema(description = "Offer title", example = "BMW X5 3.0d xDrive - Perfect condition!", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(description = "Detailed offer description",
            example = "Selling BMW X5 in excellent technical condition. First owner, serviced at authorized dealer.",
            required = true)
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @Schema(description = "Vehicle price", example = "125000.00", required = true)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(description = "Currency code", example = "PLN", required = true)
    @NotBlank(message = "Currency is required")
    private String currency;

    @Schema(description = "Whether the price is negotiable", example = "true", defaultValue = "true")
    private boolean negotiable = true;

    @Schema(description = "Whether the offer should be featured", example = "false", defaultValue = "false")
    private boolean featured = false;

    @Schema(description = "Vehicle location", example = "Warsaw, Mazovian Voivodeship")
    private String location;

    @Schema(description = "Contact phone number", example = "+48 123 456 789")
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
            message = "Invalid phone number format")
    private String contactPhone;

    @Schema(description = "Contact email address", example = "seller@example.com")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @Schema(description = "Offer expiration date", example = "2024-04-15T23:59:59")
    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;

    // Vehicle Details
    @Schema(description = "Vehicle brand", example = "BMW", required = true)
    @NotBlank(message = "Brand is required")
    private String brand;

    @Schema(description = "Vehicle model", example = "X5", required = true)
    @NotBlank(message = "Model is required")
    private String model;

    @Schema(description = "Production year", example = "2020", required = true)
    @NotNull(message = "Production year is required")
    private Integer year;

    @Schema(description = "Vehicle color", example = "Black")
    private String color;

    @Schema(description = "Engine displacement", example = "3.0")
    private String displacement;

    @Schema(description = "Vehicle Identification Number", example = "WBAFV13506L123456")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN number format")
    private String vin;

    @Schema(description = "Vehicle mileage in kilometers", example = "150000", minimum = "0")
    private Integer mileage;

    @Schema(description = "Type of fuel used by the vehicle")
    private FuelType fuelType;

    @Schema(description = "Type of transmission")
    private TransmissionType transmission;

    @Schema(description = "Vehicle body type")
    private BodyType bodyType;

    @Schema(description = "Drive type (FWD, RWD, AWD)")
    private DriveType driveType;

    @Schema(description = "Engine power in horsepower", example = "250", minimum = "0")
    private Integer enginePower;

    @Schema(description = "Number of doors", example = "5", minimum = "2", maximum = "6")
    private Integer doors;

    @Schema(description = "Number of seats", example = "5", minimum = "2", maximum = "9")
    private Integer seats;

    @Schema(description = "Vehicle condition")
    private VehicleCondition condition;

    @Schema(description = "Registration plate number", example = "WB12345")
    private String registrationNumber;

    @Schema(description = "Country of registration", example = "Poland")
    private String registrationCountry;

    @Schema(description = "Whether seller is the first owner", example = "true")
    private Boolean firstOwner;

    @Schema(description = "Whether vehicle is accident-free", example = "true")
    private Boolean accidentFree;

    @Schema(description = "Whether vehicle has complete service history", example = "true")
    private Boolean serviceHistory;

    @Schema(description = "Additional features and equipment description",
            example = "GPS Navigation, Parking Assistant, Leather Seats")
    @Size(max = 1000, message = "Additional features cannot exceed 1000 characters")
    private String additionalFeatures;

    @Schema(description = "Detailed vehicle equipment specifications")
    @Valid
    private CarEquipmentDto equipment;

    @Schema(description = "List of image UUIDs",
            example = "[\"123e4567-e89b-12d3-a456-426614174000\", \"123e4567-e89b-12d3-a456-426614174001\"]")
    @Size(min = 1, max = 10, message = "You can add between 1 and 10 images")
    private List<UUID> images;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Vehicle equipment and features")
    public static class CarEquipmentDto {

        // Comfort Features
        @Schema(description = "Air conditioning system", example = "true")
        private Boolean airConditioning;

        @Schema(description = "Automatic climate control", example = "true")
        private Boolean automaticClimate;

        @Schema(description = "Heated seats", example = "true")
        private Boolean heatedSeats;

        @Schema(description = "Electric seat adjustment", example = "false")
        private Boolean electricSeats;

        @Schema(description = "Leather seat upholstery", example = "true")
        private Boolean leatherSeats;

        @Schema(description = "Panoramic roof/sunroof", example = "true")
        private Boolean panoramicRoof;

        @Schema(description = "Electric windows", example = "true")
        private Boolean electricWindows;

        @Schema(description = "Electric mirrors", example = "true")
        private Boolean electricMirrors;

        @Schema(description = "Keyless entry system", example = "true")
        private Boolean keylessEntry;

        @Schema(description = "Heated steering wheel", example = "false")
        private Boolean wheelHeating;

        // Entertainment & Navigation
        @Schema(description = "GPS navigation system", example = "true")
        private Boolean navigationSystem;

        @Schema(description = "Bluetooth connectivity", example = "true")
        private Boolean bluetooth;

        @Schema(description = "USB ports", example = "true")
        private Boolean usbPort;

        @Schema(description = "Multifunction steering wheel", example = "true")
        private Boolean multifunction;

        @Schema(description = "Android Auto support", example = "true")
        private Boolean androidAuto;

        @Schema(description = "Apple CarPlay support", example = "true")
        private Boolean appleCarPlay;

        @Schema(description = "Premium sound system", example = "false")
        private Boolean soundSystem;

        // Safety & Driver Assistance
        @Schema(description = "Parking distance sensors", example = "true")
        private Boolean parkingSensors;

        @Schema(description = "Rear view camera", example = "true")
        private Boolean rearCamera;

        @Schema(description = "Cruise control", example = "true")
        private Boolean cruiseControl;

        @Schema(description = "Adaptive cruise control", example = "false")
        private Boolean adaptiveCruiseControl;

        @Schema(description = "Lane keeping assist", example = "false")
        private Boolean laneAssist;

        @Schema(description = "Blind spot detection", example = "false")
        private Boolean blindSpotDetection;

        @Schema(description = "Emergency braking system", example = "false")
        private Boolean emergencyBraking;

        @Schema(description = "Start-stop system", example = "true")
        private Boolean startStop;

        // Lighting
        @Schema(description = "Xenon headlights", example = "false")
        private Boolean xenonLights;

        @Schema(description = "LED headlights", example = "true")
        private Boolean ledLights;

        @Schema(description = "Ambient interior lighting", example = "true")
        private Boolean ambientLighting;

        @Schema(description = "Automatic headlight control", example = "true")
        private Boolean automaticLights;

        @Schema(description = "Adaptive headlights", example = "false")
        private Boolean adaptiveLights;

        // Additional Features
        @Schema(description = "Heated steering wheel", example = "false")
        private Boolean heatedSteeringWheel;

        @Schema(description = "Electric trunk/tailgate", example = "true")
        private Boolean electricTrunk;

        @Schema(description = "Electric sun blind", example = "false")
        private Boolean electricSunBlind;

        @Schema(description = "Head-up display", example = "false")
        private Boolean headUpDisplay;

        @Schema(description = "Aromatherapy system", example = "false")
        private Boolean aromatherapy;
    }
}