package pl.pjwstk.kodabackend.offer.model.command;

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
import pl.pjwstk.kodabackend.offer.persistance.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistance.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistance.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistance.entity.VehicleCondition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferCommand {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Currency is required")
    private String currency;

    private boolean negotiable = true;
    private boolean featured = false;

    private String location;

    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
            message = "Invalid phone number format")
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;

    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Production year is required")
    private Integer year;

    private String color;
    private String displacement;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN number format")
    private String vin;

    private Integer mileage;

    private FuelType fuelType;
    private TransmissionType transmission;
    private BodyType bodyType;
    private DriveType driveType;

    private Integer enginePower;
    private Integer doors;
    private Integer seats;

    private VehicleCondition condition;

    private String registrationNumber;
    private String registrationCountry;

    private Boolean firstOwner;
    private Boolean accidentFree;
    private Boolean serviceHistory;

    @Size(max = 1000, message = "Additional features cannot exceed 1000 characters")
    private String additionalFeatures;

    @Valid
    private CarEquipmentDto equipment;

    @Size(min = 1, max = 10, message = "You can add between 1 and 10 images")
    private List<UUID> images;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CarEquipmentDto {
        private Boolean airConditioning;
        private Boolean automaticClimate;
        private Boolean heatedSeats;
        private Boolean electricSeats;
        private Boolean leatherSeats;
        private Boolean panoramicRoof;
        private Boolean electricWindows;
        private Boolean electricMirrors;
        private Boolean keylessEntry;
        private Boolean wheelHeating;

        private Boolean navigationSystem;
        private Boolean bluetooth;
        private Boolean usbPort;
        private Boolean multifunction;
        private Boolean androidAuto;
        private Boolean appleCarPlay;
        private Boolean soundSystem;

        private Boolean parkingSensors;
        private Boolean rearCamera;
        private Boolean cruiseControl;
        private Boolean adaptiveCruiseControl;
        private Boolean laneAssist;
        private Boolean blindSpotDetection;
        private Boolean emergencyBraking;
        private Boolean startStop;

        private Boolean xenonLights;
        private Boolean ledLights;
        private Boolean ambientLighting;
        private Boolean automaticLights;
        private Boolean adaptiveLights;

        private Boolean heatedSteeringWheel;
        private Boolean electricTrunk;
        private Boolean electricSunBlind;
        private Boolean headUpDisplay;
        private Boolean aromatherapy;
    }
}