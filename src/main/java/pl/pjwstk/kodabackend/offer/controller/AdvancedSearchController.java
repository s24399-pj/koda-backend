package pl.pjwstk.kodabackend.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;

@RequestMapping("/api/v1/offers/search")
@Tag(name = "Advanced Offer Search", description = "APIs for advanced searching and filtering of offers")
public interface AdvancedSearchController {

    @PostMapping("/advanced")
    @Operation(summary = "Search offers with advanced filtering",
            description = "Filter offers using any combination of criteria including car details and equipment")
    ResponseEntity<Page<OfferDetailedDto>> advancedSearch(
            @RequestBody AdvancedSearchRequest request,
            Pageable pageable);

    @GetMapping("/brands")
    @Operation(summary = "Get all car brands", description = "Returns a list of all car brands available in the system")
    ResponseEntity<Page<String>> getBrands(Pageable pageable);

    @GetMapping("/brands/search")
    @Operation(summary = "Search car brands", description = "Search for car brands by phrase")
    ResponseEntity<Page<String>> searchBrands(
            @RequestParam String phrase,
            Pageable pageable);

    @GetMapping("/fuel-types")
    @Operation(summary = "Get all fuel types", description = "Returns a list of all available fuel types")
    ResponseEntity<Page<String>> getFuelTypes(Pageable pageable);

    @GetMapping("/transmission-types")
    @Operation(summary = "Get all transmission types", description = "Returns a list of all available transmission types")
    ResponseEntity<Page<String>> getTransmissionTypes(Pageable pageable);

    @GetMapping("/body-types")
    @Operation(summary = "Get all body types", description = "Returns a list of all available body types")
    ResponseEntity<Page<String>> getBodyTypes(Pageable pageable);

    @GetMapping("/drive-types")
    @Operation(summary = "Get all drive types", description = "Returns a list of all available drive types")
    ResponseEntity<Page<String>> getDriveTypes(Pageable pageable);

    @GetMapping("/vehicle-conditions")
    @Operation(summary = "Get all vehicle conditions", description = "Returns a list of all available vehicle conditions")
    ResponseEntity<Page<String>> getVehicleConditions(Pageable pageable);
}