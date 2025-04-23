package pl.pjwstk.kodabackend.offer.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

/**
 * Handler for searching offers by user/owner ID only.
 */
@Slf4j
@Component
public class UserOfferSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public UserOfferSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasUserId() && !request.hasPhrase() && !request.hasAnyPriceFilter()) {
            log.debug("Searching offers by user ID: {}", request.getUserId());
            return offerRepository.findByUserId(request.getUserId(), request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}