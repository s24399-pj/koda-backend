package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.model.AdvancedOfferSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.persistance.repository.AdvancedOfferRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvancedOfferSearchService {

    private final AdvancedOfferRepository advancedOfferRepository;

    @Transactional(readOnly = true)
    public Page<OfferMiniDto> searchOffers(AdvancedOfferSearchRequest request) {
        log.debug("Executing simple offer search with filters: {}", request);

        // Sprawdź czy są filtry wyposażenia
        if (request.getAirConditioning() != null ||
                request.getNavigationSystem() != null ||
                request.getBluetooth() != null) {

            // Użyj zapytania z wyposażeniem
            return advancedOfferRepository.findOffersWithEquipmentFilters(
                    request.getSearchTerm(),
                    request.getPriceFrom(),
                    request.getPriceTo(),
                    request.getBrand(),
                    request.getAirConditioning(),
                    request.getNavigationSystem(),
                    request.getBluetooth(),
                    request.getPageable()
            );
        } else {
            // Użyj podstawowego zapytania
            return advancedOfferRepository.findOffersWithBasicFilters(
                    request.getSearchTerm(),
                    request.getPriceFrom(),
                    request.getPriceTo(),
                    request.getBrand(),
                    request.getModel(),
                    request.getYearFrom(),
                    request.getYearTo(),
                    request.getMileageFrom(),
                    request.getMileageTo(),
                    request.getEnginePowerFrom(),
                    request.getEnginePowerTo(),
                    request.getPageable()
            );
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFilterOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("brands", advancedOfferRepository.findAllBrands());
        return options;
    }

    @Transactional(readOnly = true)
    public List<String> getModelsByBrand(String brand) {
        return advancedOfferRepository.findModelsByBrand(brand);
    }
}