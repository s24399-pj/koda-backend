package pl.pjwstk.kodabackend.offer.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

/**
 * Handler for searching offers by phrase and user/owner ID.
 */
@Slf4j
@Component
public class PhraseAndUserSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public PhraseAndUserSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasPhrase() && request.hasUserId() && !request.hasAnyPriceFilter()) {
            log.debug("Searching offers by phrase '{}' and user ID: {}",
                    request.getPhrase(), request.getUserId());
            return offerRepository.findByPhraseAndUserId(
                    request.getPhrase(),
                    request.getUserId(),
                    request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}