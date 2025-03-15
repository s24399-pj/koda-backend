package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.offer.model.OfferDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Validated
class OfferController {
    private final OfferService offerService;

    @GetMapping("/{id}")
    public OfferDto findOfferById(@PathVariable UUID id) {
        return offerService.findOfferById(id);
    }

}
