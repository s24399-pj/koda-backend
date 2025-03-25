package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.service.OfferMiniService;
import pl.pjwstk.kodabackend.offer.service.OfferService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static pl.pjwstk.kodabackend.offer.service.OfferMiniService.sortingAliasProcessor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
@Validated
class OfferControllerImpl implements OfferController {
    private final OfferService offerService;
    private final OfferMiniService offerMiniService;

    @GetMapping
    @Override
    public Page<OfferMiniDto> findAllMini(Pageable pageable, String phrase, BigDecimal minPrice, BigDecimal maxPrice) {
        return offerMiniService.findAllOfferMini(sortingAliasProcessor(pageable),
                phrase,
                minPrice,
                maxPrice);
    }

    @GetMapping("/find")
    @Override
    public List<String> findOfferNamesByPhrase(String phrase) {
        return offerService.findOfferNamesByPhrase(phrase);
    }

    @GetMapping("/{id}")
    @Override
    public OfferDto findOfferById(UUID id) {
        return offerService.findOfferById(id);
    }
}
