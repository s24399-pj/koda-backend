package pl.pjwstk.kodabackend.offer.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "car_equipment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CarEquipment {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "carEquipment")
    private CarDetails carDetails;

    // Komfort
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

    // Multimedia
    private Boolean navigationSystem;
    private Boolean bluetooth;
    private Boolean usbPort;
    private Boolean multifunction;
    private Boolean androidAuto;
    private Boolean appleCarPlay;
    private Boolean soundSystem;

    // Systemy wspomagające
    private Boolean parkingSensors;
    private Boolean rearCamera;
    private Boolean cruiseControl;
    private Boolean adaptiveCruiseControl;
    private Boolean laneAssist;
    private Boolean blindSpotDetection;
    private Boolean emergencyBraking;
    private Boolean startStop;

    // Oświetlenie
    private Boolean xenonLights;
    private Boolean ledLights;
    private Boolean ambientLighting;
    private Boolean automaticLights;
    private Boolean adaptiveLights;

    // Dodatkowe funkcje
    private Boolean heatedSteeringWheel;
    private Boolean electricTrunk;
    private Boolean electricSunBlind;
    private Boolean headUpDisplay;
    private Boolean aromatherapy;
}
