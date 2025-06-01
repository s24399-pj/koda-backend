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

            // Title or description search
            if (request.getPhrase() != null && !request.getPhrase().trim().isEmpty()) {
                String searchPhrase = "%" + request.getPhrase().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), searchPhrase),
                        cb.like(cb.lower(root.get("description")), searchPhrase)
                ));
            }

            // Price range
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            // User ID filter
            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("seller").get("id"), request.getUserId()));
            }

            // Car Details filters
            addCarDetailsFilters(request, carDetailsJoin, predicates, cb);

            // Car Equipment filters
            addCarEquipmentFilters(request, carEquipmentJoin, predicates, cb);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addCarDetailsFilters(AdvancedSearchRequest request, Join<Offer, CarDetails> carDetailsJoin,
                                      List<Predicate> predicates, jakarta.persistence.criteria.CriteriaBuilder cb) {
        // Brand filter
        if (request.getBrand() != null && !request.getBrand().isEmpty()) {
            predicates.add(cb.equal(carDetailsJoin.get("brand"), request.getBrand()));
        }

        // Model filter
        if (request.getModel() != null && !request.getModel().isEmpty()) {
            predicates.add(cb.equal(carDetailsJoin.get("model"), request.getModel()));
        }

        // Year range
        if (request.getMinYear() != null) {
            predicates.add(cb.greaterThanOrEqualTo(carDetailsJoin.get("year"), request.getMinYear()));
        }
        if (request.getMaxYear() != null) {
            predicates.add(cb.lessThanOrEqualTo(carDetailsJoin.get("year"), request.getMaxYear()));
        }

        // Mileage range
        if (request.getMinMileage() != null) {
            predicates.add(cb.greaterThanOrEqualTo(carDetailsJoin.get("mileage"), request.getMinMileage()));
        }
        if (request.getMaxMileage() != null) {
            predicates.add(cb.lessThanOrEqualTo(carDetailsJoin.get("mileage"), request.getMaxMileage()));
        }

        // Engine power range
        if (request.getMinEnginePower() != null) {
            predicates.add(cb.greaterThanOrEqualTo(carDetailsJoin.get("enginePower"), request.getMinEnginePower()));
        }
        if (request.getMaxEnginePower() != null) {
            predicates.add(cb.lessThanOrEqualTo(carDetailsJoin.get("enginePower"), request.getMaxEnginePower()));
        }

        // Fuel type
        if (request.getFuelType() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("fuelType"), request.getFuelType()));
        }

        // Transmission type
        if (request.getTransmission() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("transmission"), request.getTransmission()));
        }

        // Body type
        if (request.getBodyType() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("bodyType"), request.getBodyType()));
        }

        // Drive type
        if (request.getDriveType() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("driveType"), request.getDriveType()));
        }

        // Vehicle condition
        if (request.getCondition() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("condition"), request.getCondition()));
        }

        // Boolean filters
        if (request.getFirstOwner() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("firstOwner"), request.getFirstOwner()));
        }
        if (request.getAccidentFree() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("accidentFree"), request.getAccidentFree()));
        }
        if (request.getServiceHistory() != null) {
            predicates.add(cb.equal(carDetailsJoin.get("serviceHistory"), request.getServiceHistory()));
        }
    }

    private void addCarEquipmentFilters(AdvancedSearchRequest request, Join<CarDetails, CarEquipment> carEquipmentJoin,
                                        List<Predicate> predicates, jakarta.persistence.criteria.CriteriaBuilder cb) {
        // Comfort features
        addBooleanFilter(carEquipmentJoin, predicates, cb, "airConditioning", request.getAirConditioning());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "automaticClimate", request.getAutomaticClimate());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "heatedSeats", request.getHeatedSeats());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "electricSeats", request.getElectricSeats());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "leatherSeats", request.getLeatherSeats());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "panoramicRoof", request.getPanoramicRoof());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "electricWindows", request.getElectricWindows());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "electricMirrors", request.getElectricMirrors());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "keylessEntry", request.getKeylessEntry());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "wheelHeating", request.getWheelHeating());

        // Multimedia features
        addBooleanFilter(carEquipmentJoin, predicates, cb, "navigationSystem", request.getNavigationSystem());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "bluetooth", request.getBluetooth());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "usbPort", request.getUsbPort());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "multifunction", request.getMultifunction());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "androidAuto", request.getAndroidAuto());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "appleCarPlay", request.getAppleCarPlay());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "soundSystem", request.getSoundSystem());

        // Assistance systems
        addBooleanFilter(carEquipmentJoin, predicates, cb, "parkingSensors", request.getParkingSensors());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "rearCamera", request.getRearCamera());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "cruiseControl", request.getCruiseControl());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "adaptiveCruiseControl", request.getAdaptiveCruiseControl());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "laneAssist", request.getLaneAssist());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "blindSpotDetection", request.getBlindSpotDetection());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "emergencyBraking", request.getEmergencyBraking());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "startStop", request.getStartStop());

        // Lighting
        addBooleanFilter(carEquipmentJoin, predicates, cb, "xenonLights", request.getXenonLights());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "ledLights", request.getLedLights());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "ambientLighting", request.getAmbientLighting());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "automaticLights", request.getAutomaticLights());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "adaptiveLights", request.getAdaptiveLights());

        // Additional features
        addBooleanFilter(carEquipmentJoin, predicates, cb, "heatedSteeringWheel", request.getHeatedSteeringWheel());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "electricTrunk", request.getElectricTrunk());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "electricSunBlind", request.getElectricSunBlind());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "headUpDisplay", request.getHeadUpDisplay());
        addBooleanFilter(carEquipmentJoin, predicates, cb, "aromatherapy", request.getAromatherapy());
    }

    private void addBooleanFilter(Join<CarDetails, CarEquipment> join, List<Predicate> predicates,
                                  jakarta.persistence.criteria.CriteriaBuilder cb, String field, Boolean value) {
        if (value != null) {
            predicates.add(cb.equal(join.get(field), value));
        }
    }
}