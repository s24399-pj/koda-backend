package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.persistance.entity.BodyType;
import pl.pjwstk.kodabackend.offer.persistance.entity.DriveType;
import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistance.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistance.entity.VehicleCondition;
import pl.pjwstk.kodabackend.offer.persistance.repository.CarDetailsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service providing options for filter dropdowns in the UI.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilterOptionsService {

    private final CarDetailsRepository carDetailsRepository;

    /**
     * Get all available car brands.
     */
    @Transactional(readOnly = true)
    public Page<String> getAllBrands(Pageable pageable) {
        return carDetailsRepository.findAllBrandsPageable(pageable);
    }

    /**
     * Search for brands by phrase.
     */
    @Transactional(readOnly = true)
    public Page<String> searchBrands(String phrase, Pageable pageable) {
        return carDetailsRepository.findBrandsByPhrasePageable(phrase, pageable);
    }

    /**
     * Get all possible fuel types.
     */
    public Page<String> getFuelTypes(Pageable pageable) {
        List<String> fuelTypes = Arrays.stream(FuelType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return createEnumPage(fuelTypes, pageable);
    }

    /**
     * Get all possible transmission types.
     */
    public Page<String> getTransmissionTypes(Pageable pageable) {
        List<String> transmissionTypes = Arrays.stream(TransmissionType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return createEnumPage(transmissionTypes, pageable);
    }

    /**
     * Get all possible body types.
     */
    public Page<String> getBodyTypes(Pageable pageable) {
        List<String> bodyTypes = Arrays.stream(BodyType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return createEnumPage(bodyTypes, pageable);
    }

    /**
     * Get all possible drive types.
     */
    public Page<String> getDriveTypes(Pageable pageable) {
        List<String> driveTypes = Arrays.stream(DriveType.values())
                .map(DriveType::getDisplayName)
                .collect(Collectors.toList());
        return createEnumPage(driveTypes, pageable);
    }

    /**
     * Get all possible vehicle conditions.
     */
    public Page<String> getVehicleConditions(Pageable pageable) {
        List<String> conditions = Arrays.stream(VehicleCondition.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return createEnumPage(conditions, pageable);
    }

    /**
     * Helper method to create a Page from enum values.
     * This is reasonable for enums since they have a small, fixed number of values.
     */
    private <T> Page<T> createEnumPage(List<T> enumValues, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), enumValues.size());

        if (start >= enumValues.size()) {
            return Page.empty(pageable);
        }

        return new PageImpl<>(enumValues.subList(start, end), pageable, enumValues.size());
    }
}