package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.*;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvancedOfferSearchService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    /**
     * Search for offers with advanced filtering criteria
     */
    @Transactional(readOnly = true)
    public Page<OfferDetailedDto> searchOffers(AdvancedSearchRequest request, Pageable pageable) {
        log.debug("Executing advanced search with criteria: {}", request);

        Specification<Offer> spec = buildSpecification(request);

        Page<Offer> offers = offerRepository.findAll(spec, pageable);
        return offers.map(offerMapper::mapToOfferDetailedDto);
    }

    /**
     * Build a JPA Specification for dynamic querying based on search parameters
     */
    private Specification<Offer> buildSpecification(AdvancedSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join related entities for filtering
            Join<Offer, CarDetails> carDetailsJoin = root.join("carDetails", JoinType.INNER);
            Join<CarDetails, CarEquipment> carEquipmentJoin = carDetailsJoin.join("carEquipment", JoinType.INNER);

            // Apply filters in a structured way
            applyBasicSearchFilters(request, root, predicates, cb);
            applyCarDetailsFilters(request, carDetailsJoin, predicates, cb);
            applyCarEquipmentFilters(request, carEquipmentJoin, predicates, cb);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Apply basic offer filters (title, description, price, user)
     */
    private void applyBasicSearchFilters(AdvancedSearchRequest request,
                                         jakarta.persistence.criteria.Root<Offer> root,
                                         List<Predicate> predicates,
                                         jakarta.persistence.criteria.CriteriaBuilder cb) {
        // Title or description search
        if (request.getPhrase() != null && !request.getPhrase().trim().isEmpty()) {
            String searchPhrase = "%" + request.getPhrase().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("title")), searchPhrase),
                    cb.like(cb.lower(root.get("description")), searchPhrase)
            ));
        }

        // Price range
        applyRangeFilter(root, predicates, cb, "price", request.getMinPrice(), request.getMaxPrice());

        // User ID filter
        if (request.getUserId() != null) {
            predicates.add(cb.equal(root.get("seller").get("id"), request.getUserId()));
        }
    }

    /**
     * Apply car details filters
     */
    private void applyCarDetailsFilters(AdvancedSearchRequest request,
                                        Join<Offer, CarDetails> carDetailsJoin,
                                        List<Predicate> predicates,
                                        jakarta.persistence.criteria.CriteriaBuilder cb) {
        // String filters
        applyStringFilter(carDetailsJoin, predicates, cb, "brand", request.getBrand());
        applyStringFilter(carDetailsJoin, predicates, cb, "model", request.getModel());

        // Range filters
        applyRangeFilter(carDetailsJoin, predicates, cb, "year", request.getMinYear(), request.getMaxYear());
        applyRangeFilter(carDetailsJoin, predicates, cb, "mileage", request.getMinMileage(), request.getMaxMileage());
        applyRangeFilter(carDetailsJoin, predicates, cb, "enginePower", request.getMinEnginePower(), request.getMaxEnginePower());

        // Enum filters
        applyEnumFilter(carDetailsJoin, predicates, cb, "fuelType", request.getFuelType());
        applyEnumFilter(carDetailsJoin, predicates, cb, "transmission", request.getTransmission());
        applyEnumFilter(carDetailsJoin, predicates, cb, "bodyType", request.getBodyType());
        applyEnumFilter(carDetailsJoin, predicates, cb, "driveType", request.getDriveType());
        applyEnumFilter(carDetailsJoin, predicates, cb, "condition", request.getCondition());

        // Boolean filters
        applyBooleanFilter(carDetailsJoin, predicates, cb, "firstOwner", request.getFirstOwner());
        applyBooleanFilter(carDetailsJoin, predicates, cb, "accidentFree", request.getAccidentFree());
        applyBooleanFilter(carDetailsJoin, predicates, cb, "serviceHistory", request.getServiceHistory());
    }

    /**
     * Apply car equipment filters using groups for better organization
     */
    private void applyCarEquipmentFilters(AdvancedSearchRequest request,
                                          Join<CarDetails, CarEquipment> carEquipmentJoin,
                                          List<Predicate> predicates,
                                          jakarta.persistence.criteria.CriteriaBuilder cb) {
        // Apply filters by feature groups
        applyComfortFeatures(request, carEquipmentJoin, predicates, cb);
        applyMultimediaFeatures(request, carEquipmentJoin, predicates, cb);
        applyAssistanceFeatures(request, carEquipmentJoin, predicates, cb);
        applyLightingFeatures(request, carEquipmentJoin, predicates, cb);
        applyAdditionalFeatures(request, carEquipmentJoin, predicates, cb);
    }

    /**
     * Apply comfort feature filters
     */
    private void applyComfortFeatures(AdvancedSearchRequest request,
                                      Join<CarDetails, CarEquipment> join,
                                      List<Predicate> predicates,
                                      jakarta.persistence.criteria.CriteriaBuilder cb) {
        applyBooleanFilter(join, predicates, cb, "airConditioning", request.getAirConditioning());
        applyBooleanFilter(join, predicates, cb, "automaticClimate", request.getAutomaticClimate());
        applyBooleanFilter(join, predicates, cb, "heatedSeats", request.getHeatedSeats());
        applyBooleanFilter(join, predicates, cb, "electricSeats", request.getElectricSeats());
        applyBooleanFilter(join, predicates, cb, "leatherSeats", request.getLeatherSeats());
        applyBooleanFilter(join, predicates, cb, "panoramicRoof", request.getPanoramicRoof());
        applyBooleanFilter(join, predicates, cb, "electricWindows", request.getElectricWindows());
        applyBooleanFilter(join, predicates, cb, "electricMirrors", request.getElectricMirrors());
        applyBooleanFilter(join, predicates, cb, "keylessEntry", request.getKeylessEntry());
        applyBooleanFilter(join, predicates, cb, "wheelHeating", request.getWheelHeating());
    }

    /**
     * Apply multimedia feature filters
     */
    private void applyMultimediaFeatures(AdvancedSearchRequest request,
                                         Join<CarDetails, CarEquipment> join,
                                         List<Predicate> predicates,
                                         jakarta.persistence.criteria.CriteriaBuilder cb) {
        applyBooleanFilter(join, predicates, cb, "navigationSystem", request.getNavigationSystem());
        applyBooleanFilter(join, predicates, cb, "bluetooth", request.getBluetooth());
        applyBooleanFilter(join, predicates, cb, "usbPort", request.getUsbPort());
        applyBooleanFilter(join, predicates, cb, "multifunction", request.getMultifunction());
        applyBooleanFilter(join, predicates, cb, "androidAuto", request.getAndroidAuto());
        applyBooleanFilter(join, predicates, cb, "appleCarPlay", request.getAppleCarPlay());
        applyBooleanFilter(join, predicates, cb, "soundSystem", request.getSoundSystem());
    }

    /**
     * Apply assistance feature filters
     */
    private void applyAssistanceFeatures(AdvancedSearchRequest request,
                                         Join<CarDetails, CarEquipment> join,
                                         List<Predicate> predicates,
                                         jakarta.persistence.criteria.CriteriaBuilder cb) {
        applyBooleanFilter(join, predicates, cb, "parkingSensors", request.getParkingSensors());
        applyBooleanFilter(join, predicates, cb, "rearCamera", request.getRearCamera());
        applyBooleanFilter(join, predicates, cb, "cruiseControl", request.getCruiseControl());
        applyBooleanFilter(join, predicates, cb, "adaptiveCruiseControl", request.getAdaptiveCruiseControl());
        applyBooleanFilter(join, predicates, cb, "laneAssist", request.getLaneAssist());
        applyBooleanFilter(join, predicates, cb, "blindSpotDetection", request.getBlindSpotDetection());
        applyBooleanFilter(join, predicates, cb, "emergencyBraking", request.getEmergencyBraking());
        applyBooleanFilter(join, predicates, cb, "startStop", request.getStartStop());
    }

    /**
     * Apply lighting feature filters
     */
    private void applyLightingFeatures(AdvancedSearchRequest request,
                                       Join<CarDetails, CarEquipment> join,
                                       List<Predicate> predicates,
                                       jakarta.persistence.criteria.CriteriaBuilder cb) {
        applyBooleanFilter(join, predicates, cb, "xenonLights", request.getXenonLights());
        applyBooleanFilter(join, predicates, cb, "ledLights", request.getLedLights());
        applyBooleanFilter(join, predicates, cb, "ambientLighting", request.getAmbientLighting());
        applyBooleanFilter(join, predicates, cb, "automaticLights", request.getAutomaticLights());
        applyBooleanFilter(join, predicates, cb, "adaptiveLights", request.getAdaptiveLights());
    }

    /**
     * Apply additional feature filters
     */
    private void applyAdditionalFeatures(AdvancedSearchRequest request,
                                         Join<CarDetails, CarEquipment> join,
                                         List<Predicate> predicates,
                                         jakarta.persistence.criteria.CriteriaBuilder cb) {
        applyBooleanFilter(join, predicates, cb, "heatedSteeringWheel", request.getHeatedSteeringWheel());
        applyBooleanFilter(join, predicates, cb, "electricTrunk", request.getElectricTrunk());
        applyBooleanFilter(join, predicates, cb, "electricSunBlind", request.getElectricSunBlind());
        applyBooleanFilter(join, predicates, cb, "headUpDisplay", request.getHeadUpDisplay());
        applyBooleanFilter(join, predicates, cb, "aromatherapy", request.getAromatherapy());
    }

    /**
     * Helper method to apply string filter
     */
    private <T, J> void applyStringFilter(J join, List<Predicate> predicates,
                                          jakarta.persistence.criteria.CriteriaBuilder cb,
                                          String field, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.equal(((jakarta.persistence.criteria.Path<T>)join).get(field), value));
        }
    }

    /**
     * Helper method to apply boolean filter
     */
    private <T, J> void applyBooleanFilter(J join, List<Predicate> predicates,
                                           jakarta.persistence.criteria.CriteriaBuilder cb,
                                           String field, Boolean value) {
        if (value != null) {
            predicates.add(cb.equal(((jakarta.persistence.criteria.Path<T>)join).get(field), value));
        }
    }

    /**
     * Helper method to apply enum filter
     */
    private <T, J, E> void applyEnumFilter(J join, List<Predicate> predicates,
                                           jakarta.persistence.criteria.CriteriaBuilder cb,
                                           String field, E value) {
        if (value != null) {
            predicates.add(cb.equal(((jakarta.persistence.criteria.Path<T>)join).get(field), value));
        }
    }

    /**
     * Helper method to apply range filter
     */
    private <T, J, N extends Comparable<? super N>> void applyRangeFilter(
            J join, List<Predicate> predicates,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            String field, N minValue, N maxValue) {

        if (minValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    ((jakarta.persistence.criteria.Path<T>)join).<N>get(field), minValue));
        }

        if (maxValue != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    ((jakarta.persistence.criteria.Path<T>)join).<N>get(field), maxValue));
        }
    }
}