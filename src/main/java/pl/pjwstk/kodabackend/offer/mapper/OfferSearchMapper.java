package pl.pjwstk.kodabackend.offer.mapper;

import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.SearchFiltersDto;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OfferSearchMapper {

    public AdvancedSearchRequest mapToAdvancedRequest(SearchFiltersDto dto) {
        if (dto == null) {
            return new AdvancedSearchRequest();
        }

        return AdvancedSearchRequest.builder()
                .phrase(dto.getSearchTerm())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .minPrice(parseToDecimal(dto.getPriceFrom()))
                .maxPrice(parseToDecimal(dto.getPriceTo()))
                .minYear(parseToInteger(dto.getYearFrom()))
                .maxYear(parseToInteger(dto.getYearTo()))
                .minMileage(parseToInteger(dto.getMileageFrom()))
                .maxMileage(parseToInteger(dto.getMileageTo()))
                .fuelType(getFirstValueFromList(dto.getFuelType()))
                .transmission(getFirstValueFromList(dto.getTransmission()))
                .bodyType(getFirstValueFromList(dto.getBodyType()))
                .driveType(getFirstValueFromList(dto.getDriveType()))
                .condition(getFirstValueFromList(dto.getCondition()))
                .minEnginePower(parseToInteger(dto.getEnginePowerFrom()))
                .maxEnginePower(parseToInteger(dto.getEnginePowerTo()))
                // Equipment fields
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

    // Helper method to extract the first element from a list
    private <T> T getFirstValueFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
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