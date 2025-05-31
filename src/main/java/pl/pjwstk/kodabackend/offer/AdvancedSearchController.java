package pl.pjwstk.kodabackend.offer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;
import pl.pjwstk.kodabackend.offer.service.AdvancedOfferSearchService;
import pl.pjwstk.kodabackend.offer.service.FilterOptionsService;

@Slf4j
@RestController
@RequestMapping("/api/offers/search")
@Tag(name = "Advanced Offer Search", description = "APIs for advanced searching and filtering of offers")
@RequiredArgsConstructor
public class AdvancedSearchController {

    private final AdvancedOfferSearchService searchService;
    private final FilterOptionsService filterOptionsService;

    @PostMapping("/advanced")
    @Operation(summary = "Search offers with advanced filtering",
            description = "Filter offers using any combination of criteria including car details and equipment")
    public ResponseEntity<Page<OfferDetailedDto>> advancedSearch(
            @RequestBody AdvancedSearchRequest request,
            Pageable pageable) {
        log.debug("Received advanced search request: {}", request);
        Page<OfferDetailedDto> results = searchService.searchOffers(request, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/brands")
    @Operation(summary = "Get all car brands", description = "Returns a list of all car brands available in the system")
    public ResponseEntity<Page<String>> getBrands(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getAllBrands(pageable));
    }

    @GetMapping("/brands/search")
    @Operation(summary = "Search car brands", description = "Search for car brands by phrase")
    public ResponseEntity<Page<String>> searchBrands(
            @RequestParam String phrase,
            Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.searchBrands(phrase, pageable));
    }

    @GetMapping("/fuel-types")
    @Operation(summary = "Get all fuel types", description = "Returns a list of all available fuel types")
    public ResponseEntity<Page<String>> getFuelTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getFuelTypes(pageable));
    }

    @GetMapping("/transmission-types")
    @Operation(summary = "Get all transmission types", description = "Returns a list of all available transmission types")
    public ResponseEntity<Page<String>> getTransmissionTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getTransmissionTypes(pageable));
    }

    @GetMapping("/body-types")
    @Operation(summary = "Get all body types", description = "Returns a list of all available body types")
    public ResponseEntity<Page<String>> getBodyTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getBodyTypes(pageable));
    }

    @GetMapping("/drive-types")
    @Operation(summary = "Get all drive types", description = "Returns a list of all available drive types")
    public ResponseEntity<Page<String>> getDriveTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getDriveTypes(pageable));
    }

    @GetMapping("/vehicle-conditions")
    @Operation(summary = "Get all vehicle conditions", description = "Returns a list of all available vehicle conditions")
    public ResponseEntity<Page<String>> getVehicleConditions(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getVehicleConditions(pageable));
    }
}