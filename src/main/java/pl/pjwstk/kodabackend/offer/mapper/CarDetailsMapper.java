package pl.pjwstk.kodabackend.offer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.CarDetailsDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.CarDetails;

@Component
@RequiredArgsConstructor
public class CarDetailsMapper {
    private final CarEquipmentMapper carEquipmentMapper;

    public CarDetailsDto mapToCarDetailsDto(CarDetails carDetails) {
        return new CarDetailsDto(
                carDetails.getId(),
                carDetails.getBrand(),
                carDetails.getModel(),
                carDetails.getYear(),
                carDetails.getColor(),
                carDetails.getDisplacement(),
                carDetails.getVin(),
                carDetails.getMileage(),
                carDetails.getFuelType(),
                carDetails.getTransmission(),
                carDetails.getBodyType(),
                carDetails.getDriveType(),
                carDetails.getEnginePower(),
                carDetails.getDoors(),
                carDetails.getSeats(),
                carDetails.getCondition(),
                carDetails.getRegistrationNumber(),
                carDetails.getRegistrationCountry(),
                Boolean.TRUE.equals(carDetails.getFirstOwner()),
                Boolean.TRUE.equals(carDetails.getAccidentFree()),
                Boolean.TRUE.equals(carDetails.getServiceHistory()),
                carDetails.getAdditionalFeatures(),
                carEquipmentMapper.mapToCarEquipmentDto(carDetails.getCarEquipment())
        );

    }
}
