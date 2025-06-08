package pl.pjwstk.kodabackend.offer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.pjwstk.kodabackend.offer.persistence.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistence.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistence.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistence.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistence.entity.VehicleCondition;

import java.util.UUID;

@Schema(description = "Detailed vehicle information")
public record CarDetailsDto(
        @Schema(description = "Unique vehicle identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Vehicle brand", example = "BMW", required = true)
        String brand,

        @Schema(description = "Vehicle model", example = "X5", required = true)
        String model,

        @Schema(description = "Production year", example = "2020", minimum = "1900", maximum = "2025")
        Integer year,

        @Schema(description = "Vehicle color", example = "Black")
        String color,

        @Schema(description = "Engine displacement", example = "3.0")
        String displacement,

        @Schema(description = "VIN number", example = "WBAFV13506L123456")
        String vin,

        @Schema(description = "Mileage in kilometers", example = "150000", minimum = "0")
        Integer mileage,

        @Schema(description = "Fuel type")
        FuelType fuelType,

        @Schema(description = "Transmission type")
        TransmissionType transmission,

        @Schema(description = "Body type")
        BodyType bodyType,

        @Schema(description = "Drive type")
        DriveType driveType,

        @Schema(description = "Engine power in HP", example = "250", minimum = "0")
        Integer enginePower,

        @Schema(description = "Number of doors", example = "5", minimum = "2", maximum = "6")
        Integer doors,

        @Schema(description = "Number of seats", example = "5", minimum = "2", maximum = "9")
        Integer seats,

        @Schema(description = "Vehicle condition")
        VehicleCondition condition,

        @Schema(description = "Registration number", example = "WB12345")
        String registrationNumber,

        @Schema(description = "Registration country", example = "Poland")
        String registrationCountry,

        @Schema(description = "Whether first owner", example = "true")
        Boolean firstOwner,

        @Schema(description = "Whether accident-free", example = "true")
        Boolean accidentFree,

        @Schema(description = "Whether has service history", example = "true")
        Boolean serviceHistory,

        @Schema(description = "Additional features and equipment",
                example = "GPS Navigation, Parking Assistant, Leather Seats")
        String additionalFeatures,

        @Schema(description = "Detailed vehicle equipment")
        CarEquipmentDto carEquipment
) {
}