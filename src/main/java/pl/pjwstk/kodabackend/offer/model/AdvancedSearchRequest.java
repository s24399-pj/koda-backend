package pl.pjwstk.kodabackend.offer.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.UUID;

/**
 * Request object for advanced search with all possible filter parameters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Advanced search request with comprehensive filtering options for vehicle offers")
public class AdvancedSearchRequest {

    // Basic search parameters
    @Schema(description = "Search phrase to match in title, description, or vehicle details",
            example = "BMW X5")
    private String phrase;

    @Schema(description = "Minimum price filter", example = "50000.00", minimum = "0")
    private BigDecimal minPrice;

    @Schema(description = "Maximum price filter", example = "200000.00", minimum = "0")
    private BigDecimal maxPrice;

    @Schema(description = "Filter by specific user/seller ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    // CarDetails filters
    @Schema(description = "Vehicle brand filter", example = "BMW")
    private String brand;

    @Schema(description = "Vehicle model filter", example = "X5")
    private String model;

    @Schema(description = "Minimum production year", example = "2018", minimum = "1900")
    private Integer minYear;

    @Schema(description = "Maximum production year", example = "2024", minimum = "1900")
    private Integer maxYear;

    @Schema(description = "Minimum mileage in kilometers", example = "0", minimum = "0")
    private Integer minMileage;

    @Schema(description = "Maximum mileage in kilometers", example = "100000", minimum = "0")
    private Integer maxMileage;

    @Schema(description = "Fuel type filter")
    private FuelType fuelType;

    @Schema(description = "Transmission type filter")
    private TransmissionType transmission;

    @Schema(description = "Body type filter")
    private BodyType bodyType;

    @Schema(description = "Drive type filter")
    private DriveType driveType;

    @Schema(description = "Minimum engine power in HP", example = "200", minimum = "0")
    private Integer minEnginePower;

    @Schema(description = "Maximum engine power in HP", example = "500", minimum = "0")
    private Integer maxEnginePower;

    @Schema(description = "Vehicle condition filter")
    private VehicleCondition condition;

    @Schema(description = "Filter for first owner vehicles only", example = "true")
    private Boolean firstOwner;

    @Schema(description = "Filter for accident-free vehicles only", example = "true")
    private Boolean accidentFree;

    @Schema(description = "Filter for vehicles with service history", example = "true")
    private Boolean serviceHistory;

    // CarEquipment filters - Comfort
    @Schema(description = "Filter for vehicles with air conditioning", example = "true")
    private Boolean airConditioning;

    @Schema(description = "Filter for vehicles with automatic climate control", example = "true")
    private Boolean automaticClimate;

    @Schema(description = "Filter for vehicles with heated seats", example = "true")
    private Boolean heatedSeats;

    @Schema(description = "Filter for vehicles with electric seat adjustment", example = "true")
    private Boolean electricSeats;

    @Schema(description = "Filter for vehicles with leather seats", example = "true")
    private Boolean leatherSeats;

    @Schema(description = "Filter for vehicles with panoramic roof/sunroof", example = "true")
    private Boolean panoramicRoof;

    @Schema(description = "Filter for vehicles with electric windows", example = "true")
    private Boolean electricWindows;

    @Schema(description = "Filter for vehicles with electric mirrors", example = "true")
    private Boolean electricMirrors;

    @Schema(description = "Filter for vehicles with keyless entry", example = "true")
    private Boolean keylessEntry;

    @Schema(description = "Filter for vehicles with heated steering wheel", example = "true")
    private Boolean wheelHeating;

    // CarEquipment filters - Multimedia
    @Schema(description = "Filter for vehicles with GPS navigation system", example = "true")
    private Boolean navigationSystem;

    @Schema(description = "Filter for vehicles with Bluetooth connectivity", example = "true")
    private Boolean bluetooth;

    @Schema(description = "Filter for vehicles with USB ports", example = "true")
    private Boolean usbPort;

    @Schema(description = "Filter for vehicles with multifunction steering wheel", example = "true")
    private Boolean multifunction;

    @Schema(description = "Filter for vehicles with Android Auto support", example = "true")
    private Boolean androidAuto;

    @Schema(description = "Filter for vehicles with Apple CarPlay support", example = "true")
    private Boolean appleCarPlay;

    @Schema(description = "Filter for vehicles with premium sound system", example = "true")
    private Boolean soundSystem;

    // CarEquipment filters - Assistance systems
    @Schema(description = "Filter for vehicles with parking distance sensors", example = "true")
    private Boolean parkingSensors;

    @Schema(description = "Filter for vehicles with rear view camera", example = "true")
    private Boolean rearCamera;

    @Schema(description = "Filter for vehicles with cruise control", example = "true")
    private Boolean cruiseControl;

    @Schema(description = "Filter for vehicles with adaptive cruise control", example = "true")
    private Boolean adaptiveCruiseControl;

    @Schema(description = "Filter for vehicles with lane keeping assist", example = "true")
    private Boolean laneAssist;

    @Schema(description = "Filter for vehicles with blind spot detection", example = "true")
    private Boolean blindSpotDetection;

    @Schema(description = "Filter for vehicles with emergency braking system", example = "true")
    private Boolean emergencyBraking;

    @Schema(description = "Filter for vehicles with start-stop system", example = "true")
    private Boolean startStop;

    // CarEquipment filters - Lighting
    @Schema(description = "Filter for vehicles with xenon headlights", example = "true")
    private Boolean xenonLights;

    @Schema(description = "Filter for vehicles with LED headlights", example = "true")
    private Boolean ledLights;

    @Schema(description = "Filter for vehicles with ambient interior lighting", example = "true")
    private Boolean ambientLighting;

    @Schema(description = "Filter for vehicles with automatic headlight control", example = "true")
    private Boolean automaticLights;

    @Schema(description = "Filter for vehicles with adaptive headlights", example = "true")
    private Boolean adaptiveLights;

    // CarEquipment filters - Additional features
    @Schema(description = "Filter for vehicles with heated steering wheel", example = "true")
    private Boolean heatedSteeringWheel;

    @Schema(description = "Filter for vehicles with electric trunk/tailgate", example = "true")
    private Boolean electricTrunk;

    @Schema(description = "Filter for vehicles with electric sun blind", example = "true")
    private Boolean electricSunBlind;

    @Schema(description = "Filter for vehicles with head-up display", example = "true")
    private Boolean headUpDisplay;

    @Schema(description = "Filter for vehicles with aromatherapy system", example = "true")
    private Boolean aromatherapy;
}