package pl.pjwstk.kodabackend.offer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.offer.controller.AdvancedSearchController;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;
import pl.pjwstk.kodabackend.offer.service.AdvancedOfferSearchService;
import pl.pjwstk.kodabackend.offer.service.FilterOptionsService;
import pl.pjwstk.kodabackend.offer.service.ModelService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
class AdvancedSearchControllerImpl implements AdvancedSearchController {

    private final AdvancedOfferSearchService searchService;
    private final ModelService modelService;
    private final FilterOptionsService filterOptionsService;

    @Override
    public ResponseEntity<Page<OfferDetailedDto>> advancedSearch(
            AdvancedSearchRequest request,
            Pageable pageable) {
        log.debug("Received advanced search request: {}", request);
        Page<OfferDetailedDto> results = searchService.searchOffers(request, pageable);
        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<Page<String>> getBrands(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getAllBrands(pageable));
    }

    @Override
    public ResponseEntity<Page<String>> searchBrands(String phrase, Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.searchBrands(phrase, pageable));
    }

    @Override
    public ResponseEntity<Page<String>> getModelsByBrand(
            @RequestParam String brand,
            Pageable pageable) {
        try {
            List<String> models = modelService.getModelsByBrand(brand);
            log.info("Found {} models for brand: {}", models.size(), brand);

            Page<String> modelPage = new PageImpl<>(models, pageable, models.size());
            return ResponseEntity.ok(modelPage);
        } catch (Exception e) {
            log.error("Error fetching models for brand: {}", brand, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Page<String>> getFuelTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getFuelTypes(pageable));
    }

    @Override
    public ResponseEntity<Page<String>> getTransmissionTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getTransmissionTypes(pageable));
    }

    @Override
    public ResponseEntity<Page<String>> getBodyTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getBodyTypes(pageable));
    }

    @Override
    public ResponseEntity<Page<String>> getDriveTypes(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getDriveTypes(pageable));
    }

    @Override
    public ResponseEntity<Page<String>> getVehicleConditions(Pageable pageable) {
        return ResponseEntity.ok(filterOptionsService.getVehicleConditions(pageable));
    }
}