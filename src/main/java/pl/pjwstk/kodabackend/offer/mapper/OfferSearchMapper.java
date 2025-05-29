package pl.pjwstk.kodabackend.offer.mapper;

import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.AdvancedOfferSearchRequest;
import pl.pjwstk.kodabackend.offer.model.SearchFiltersDto;

import java.math.BigDecimal;

@Component
public class OfferSearchMapper {

    public AdvancedOfferSearchRequest mapToAdvancedRequest(SearchFiltersDto dto) {
        if (dto == null) {
            return new AdvancedOfferSearchRequest();
        }

        return AdvancedOfferSearchRequest.builder()
                .searchTerm(dto.getSearchTerm())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .priceFrom(parseToDecimal(dto.getPriceFrom()))
                .priceTo(parseToDecimal(dto.getPriceTo()))
                .currency(dto.getCurrency())
                .yearFrom(parseToInteger(dto.getYearFrom()))
                .yearTo(parseToInteger(dto.getYearTo()))
                .mileageFrom(parseToInteger(dto.getMileageFrom()))
                .mileageTo(parseToInteger(dto.getMileageTo()))
                .location(dto.getLocation())
                .fuelType(dto.getFuelType())
                .transmission(dto.getTransmission())
                .bodyType(dto.getBodyType())
                .driveType(dto.getDriveType())
                .condition(dto.getCondition())
                .enginePowerFrom(parseToInteger(dto.getEnginePowerFrom()))
                .enginePowerTo(parseToInteger(dto.getEnginePowerTo()))
                .doors(parseToInteger(dto.getDoors()))
                .seats(parseToInteger(dto.getSeats()))
                .airConditioning(dto.getEquipment() != null ? dto.getEquipment().getAirConditioning() : null)
                .automaticClimate(dto.getEquipment() != null ? dto.getEquipment().getAutomaticClimate() : null)
                .heatedSeats(dto.getEquipment() != null ? dto.getEquipment().getHeatedSeats() : null)
                .navigationSystem(dto.getEquipment() != null ? dto.getEquipment().getNavigationSystem() : null)
                .bluetooth(dto.getEquipment() != null ? dto.getEquipment().getBluetooth() : null)
                .parkingSensors(dto.getEquipment() != null ? dto.getEquipment().getParkingSensors() : null)
                .rearCamera(dto.getEquipment() != null ? dto.getEquipment().getRearCamera() : null)
                .cruiseControl(dto.getEquipment() != null ? dto.getEquipment().getCruiseControl() : null)
                .xenonLights(dto.getEquipment() != null ? dto.getEquipment().getXenonLights() : null)
                .ledLights(dto.getEquipment() != null ? dto.getEquipment().getLedLights() : null)
                .build();
    }

    private BigDecimal parseToDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseToInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}