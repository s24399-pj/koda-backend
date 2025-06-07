package pl.pjwstk.kodabackend.offer.model;

import lombok.Data;
import pl.pjwstk.kodabackend.offer.persistance.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistance.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistance.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistance.entity.VehicleCondition;

import java.util.List;

@Data
public class SearchFiltersDto {
    private String searchTerm;
    private String brand;
    private String model;
    private String priceFrom;
    private String priceTo;
    private String currency;
    private String yearFrom;
    private String yearTo;
    private String mileageFrom;
    private String mileageTo;
    private String location;
    private List<FuelType> fuelType;
    private List<TransmissionType> transmission;
    private List<BodyType> bodyType;
    private List<DriveType> driveType;
    private List<VehicleCondition> condition;
    private String enginePowerFrom;
    private String enginePowerTo;
    private String doors;
    private String seats;
    private EquipmentDto equipment;

    @Data
    public static class EquipmentDto {
        private Boolean airConditioning;
        private Boolean automaticClimate;
        private Boolean heatedSeats;
        private Boolean navigationSystem;
        private Boolean bluetooth;
        private Boolean parkingSensors;
        private Boolean rearCamera;
        private Boolean cruiseControl;
        private Boolean xenonLights;
        private Boolean ledLights;
    }
}