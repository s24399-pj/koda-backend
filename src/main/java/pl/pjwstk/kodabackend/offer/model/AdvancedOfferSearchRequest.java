package pl.pjwstk.kodabackend.offer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import pl.pjwstk.kodabackend.offer.persistance.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedOfferSearchRequest {

    // Podstawowe wyszukiwanie
    private String searchTerm;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String currency;
    private UUID userId;
    private Pageable pageable;

    // Filtry samochodu
    private String brand;
    private String model;
    private Integer yearFrom;
    private Integer yearTo;
    private Integer mileageFrom;
    private Integer mileageTo;
    private String location;
    private List<FuelType> fuelType;
    private List<TransmissionType> transmission;
    private List<BodyType> bodyType;
    private List<DriveType> driveType;
    private List<VehicleCondition> condition;
    private Integer enginePowerFrom;
    private Integer enginePowerTo;
    private Integer doors;
    private Integer seats;

    // Filtry boolean
    private Boolean firstOwner;
    private Boolean accidentFree;
    private Boolean serviceHistory;

    // Filtry wyposażenia
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

    // Metody pomocnicze
    public boolean hasSearchTerm() {
        return searchTerm != null && !searchTerm.trim().isEmpty();
    }

    public boolean hasPriceRange() {
        return priceFrom != null || priceTo != null;
    }

    public boolean hasYearRange() {
        return yearFrom != null || yearTo != null;
    }

    public boolean hasMileageRange() {
        return mileageFrom != null || mileageTo != null;
    }

    public boolean hasEnginePowerRange() {
        return enginePowerFrom != null || enginePowerTo != null;
    }

    public boolean hasBrand() {
        return brand != null && !brand.trim().isEmpty();
    }

    public boolean hasModel() {
        return model != null && !model.trim().isEmpty();
    }

    public boolean hasLocation() {
        return location != null && !location.trim().isEmpty();
    }

    public boolean hasEquipmentFilters() {
        return airConditioning != null || automaticClimate != null || heatedSeats != null ||
                electricSeats != null || leatherSeats != null || panoramicRoof != null ||
                electricWindows != null || electricMirrors != null || keylessEntry != null ||
                wheelHeating != null || navigationSystem != null || bluetooth != null ||
                usbPort != null || multifunction != null || androidAuto != null ||
                appleCarPlay != null || soundSystem != null || parkingSensors != null ||
                rearCamera != null || cruiseControl != null || adaptiveCruiseControl != null ||
                laneAssist != null || blindSpotDetection != null || emergencyBraking != null ||
                startStop != null || xenonLights != null || ledLights != null ||
                ambientLighting != null || automaticLights != null || adaptiveLights != null ||
                heatedSteeringWheel != null || electricTrunk != null || electricSunBlind != null ||
                headUpDisplay != null || aromatherapy != null;
    }
}