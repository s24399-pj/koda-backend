package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pjwstk.kodabackend.offer.model.command.CreateOfferCommand;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.service.OfferMiniService;
import pl.pjwstk.kodabackend.offer.service.OfferService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static pl.pjwstk.kodabackend.offer.service.OfferMiniService.sortingAliasProcessor;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
@Validated
class OfferControllerImpl implements OfferController {
    private final OfferService offerService;
    private final OfferMiniService offerMiniService;

    @GetMapping
    @Override
    public Page<OfferMiniDto> findAllMini(Pageable pageable,
                                          String phrase,
                                          BigDecimal minPrice,
                                          BigDecimal maxPrice,
                                          UUID userId) {
        return offerMiniService.findAllOfferMini(sortingAliasProcessor(pageable),
                phrase,
                minPrice,
                maxPrice,
                userId);
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Override
    public OfferDto createOffer(@Validated @RequestBody CreateOfferCommand createOfferCommand, Principal principal) {
        String userEmail = principal.getName();
        return offerService.createOffer(createOfferCommand, userEmail);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deleteOffer(@PathVariable UUID id, Principal principal) {
        log.info("Otrzymano żądanie usunięcia oferty o ID: {}", id);
        String userEmail = principal.getName();
        offerService.deleteOffer(id, userEmail);
    }
}
