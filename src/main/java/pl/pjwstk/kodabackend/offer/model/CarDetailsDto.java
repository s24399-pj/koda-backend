package pl.pjwstk.kodabackend.offer.model;

import pl.pjwstk.kodabackend.offer.persistance.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistance.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistance.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistance.entity.VehicleCondition;

import java.util.UUID;

public record CarDetailsDto(
        UUID id,
        String brand,
        String model,
        Integer year,
        String color,
        String displacement,
        String vin,
        Integer mileage,
        FuelType fuelType,
        TransmissionType transmission,
        BodyType bodyType,
        DriveType driveType,
        Integer enginePower,
        Integer doors,
        Integer seats,
        VehicleCondition condition,
        String registrationNumber,
        String registrationCountry,
        Boolean firstOwner,
        Boolean accidentFree,
        Boolean serviceHistory,
        String additionalFeatures,
        CarEquipmentDto carEquipment
) {
}
