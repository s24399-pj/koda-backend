package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.offer.model.AdvancedOfferSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.*;
import pl.pjwstk.kodabackend.offer.service.AdvancedOfferSearchService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/offers/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdvancedOfferSearchController {

    private final AdvancedOfferSearchService advancedOfferSearchService;

    @PostMapping("/advanced")
    public ResponseEntity<Page<OfferMiniDto>> searchOffers(
            @RequestBody AdvancedOfferSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Advanced search request: {}", request);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        request.setPageable(pageable);

        Page<OfferMiniDto> results = advancedOfferSearchService.searchOffers(request);

        log.info("Found {} offers matching criteria", results.getTotalElements());

        return ResponseEntity.ok(results);
    }

    @GetMapping("/advanced")
    public ResponseEntity<Page<OfferMiniDto>> searchOffersGet(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Integer mileageFrom,
            @RequestParam(required = false) Integer mileageTo,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) List<FuelType> fuelType,
            @RequestParam(required = false) List<TransmissionType> transmission,
            @RequestParam(required = false) List<BodyType> bodyType,
            @RequestParam(required = false) List<DriveType> driveType,
            @RequestParam(required = false) List<VehicleCondition> condition,
            @RequestParam(required = false) Integer enginePowerFrom,
            @RequestParam(required = false) Integer enginePowerTo,
            @RequestParam(required = false) Integer doors,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) Boolean firstOwner,
            @RequestParam(required = false) Boolean accidentFree,
            @RequestParam(required = false) Boolean serviceHistory,
            @RequestParam(required = false) Boolean airConditioning,
            @RequestParam(required = false) Boolean automaticClimate,
            @RequestParam(required = false) Boolean heatedSeats,
            @RequestParam(required = false) Boolean navigationSystem,
            @RequestParam(required = false) Boolean bluetooth,
            @RequestParam(required = false) Boolean parkingSensors,
            @RequestParam(required = false) Boolean rearCamera,
            @RequestParam(required = false) Boolean cruiseControl,
            @RequestParam(required = false) Boolean xenonLights,
            @RequestParam(required = false) Boolean ledLights,
            @RequestParam(required = false) UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        AdvancedOfferSearchRequest request = AdvancedOfferSearchRequest.builder()
                .searchTerm(searchTerm)
                .priceFrom(priceFrom)
                .priceTo(priceTo)
                .currency(currency)
                .brand(brand)
                .model(model)
                .yearFrom(yearFrom)
                .yearTo(yearTo)
                .mileageFrom(mileageFrom)
                .mileageTo(mileageTo)
                .location(location)
                .fuelType(fuelType)
                .transmission(transmission)
                .bodyType(bodyType)
                .driveType(driveType)
                .condition(condition)
                .enginePowerFrom(enginePowerFrom)
                .enginePowerTo(enginePowerTo)
                .doors(doors)
                .seats(seats)
                .firstOwner(firstOwner)
                .accidentFree(accidentFree)
                .serviceHistory(serviceHistory)
                .airConditioning(airConditioning)
                .automaticClimate(automaticClimate)
                .heatedSeats(heatedSeats)
                .navigationSystem(navigationSystem)
                .bluetooth(bluetooth)
                .parkingSensors(parkingSensors)
                .rearCamera(rearCamera)
                .cruiseControl(cruiseControl)
                .xenonLights(xenonLights)
                .ledLights(ledLights)
                .userId(userId)
                .pageable(pageable)
                .build();

        Page<OfferMiniDto> results = advancedOfferSearchService.searchOffers(request);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter-options")
    public ResponseEntity<Map<String, Object>> getFilterOptions() {
        Map<String, Object> options = advancedOfferSearchService.getFilterOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/models/{brand}")
    public ResponseEntity<List<String>> getModelsByBrand(@PathVariable String brand) {
        List<String> models = advancedOfferSearchService.getModelsByBrand(brand);
        return ResponseEntity.ok(models);
    }
}