package pl.pjwstk.kodabackend.offer.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "car_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CarDetails {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String brand;
    private String model;
    private Integer year;
    private String color;

    @Column(name = "engine_displacement")
    private String displacement;

    @Column(name = "vin_number", unique = true)
    private String vin;

    private Integer mileage;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    private Integer enginePower;

    private Integer doors;
    private Integer seats;

    @Enumerated(EnumType.STRING)
    private VehicleCondition condition;

    private String registrationNumber;
    private String registrationCountry;

    private Boolean firstOwner;
    private Boolean accidentFree;
    private Boolean serviceHistory;

    @Column(length = 1000)
    private String additionalFeatures;

    @OneToOne(mappedBy = "carDetails")
    private CarEquipment carEquipment;
}