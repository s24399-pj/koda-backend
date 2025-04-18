package pl.pjwstk.kodabackend.offer.model;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferCommand {

    @NotBlank(message = "Tytuł jest wymagany")
    @Size(min = 3, max = 100, message = "Tytuł musi mieć między 3 a 100 znaków")
    private String title;

    @NotBlank(message = "Opis jest wymagany")
    @Size(min = 10, max = 2000, message = "Opis musi mieć między 10 a 2000 znaków")
    private String description;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa od 0")
    private BigDecimal price;

    @NotBlank(message = "Waluta jest wymagana")
    private String currency;

    private boolean negotiable = true;
    private boolean featured = false;

    private String location;

    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
            message = "Niepoprawny format numeru telefonu")
    private String contactPhone;

    @Email(message = "Niepoprawny format adresu email")
    private String contactEmail;

    @Future(message = "Data wygaśnięcia musi być w przyszłości")
    private LocalDateTime expirationDate;

    @NotBlank(message = "Marka jest wymagana")
    private String brand;

    @NotBlank(message = "Model jest wymagany")
    private String model;

    @NotNull(message = "Rok produkcji jest wymagany")
    private Integer year;

    private String color;
    private String displacement;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Niepoprawny format numeru VIN")
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

    @Size(max = 1000, message = "Dodatkowe wyposażenie nie może przekraczać 1000 znaków")
    private String additionalFeatures;

    @Valid
    private CarEquipmentDto equipment;

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