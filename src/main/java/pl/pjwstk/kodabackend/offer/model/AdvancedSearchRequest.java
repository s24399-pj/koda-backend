package pl.pjwstk.kodabackend.offer.model;

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
import java.util.UUID;

/**
 * Request object for advanced search with all possible filter parameters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSearchRequest {
    // Basic search parameters
    private String phrase;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private UUID userId;

    // CarDetails filters
    private String brand;
    private String model;
    private Integer minYear;
    private Integer maxYear;
    private Integer minMileage;
    private Integer maxMileage;
    private FuelType fuelType;
    private TransmissionType transmission;
    private BodyType bodyType;
    private DriveType driveType;
    private Integer minEnginePower;
    private Integer maxEnginePower;
    private VehicleCondition condition;
    private Boolean firstOwner;
    private Boolean accidentFree;
    private Boolean serviceHistory;

    // CarEquipment filters - Comfort
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

    // CarEquipment filters - Multimedia
    private Boolean navigationSystem;
    private Boolean bluetooth;
    private Boolean usbPort;
    private Boolean multifunction;
    private Boolean androidAuto;
    private Boolean appleCarPlay;
    private Boolean soundSystem;

    // CarEquipment filters - Assistance systems
    private Boolean parkingSensors;
    private Boolean rearCamera;
    private Boolean cruiseControl;
    private Boolean adaptiveCruiseControl;
    private Boolean laneAssist;
    private Boolean blindSpotDetection;
    private Boolean emergencyBraking;
    private Boolean startStop;

    // CarEquipment filters - Lighting
    private Boolean xenonLights;
    private Boolean ledLights;
    private Boolean ambientLighting;
    private Boolean automaticLights;
    private Boolean adaptiveLights;

    // CarEquipment filters - Additional features
    private Boolean heatedSteeringWheel;
    private Boolean electricTrunk;
    private Boolean electricSunBlind;
    private Boolean headUpDisplay;
    private Boolean aromatherapy;
}