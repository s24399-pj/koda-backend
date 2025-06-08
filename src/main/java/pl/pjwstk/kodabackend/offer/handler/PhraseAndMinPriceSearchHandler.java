package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

/**
 * Handler for searching offers with both phrase and minimum price filter.
 */
@Component
public class PhraseAndMinPriceSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public PhraseAndMinPriceSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasPhrase() && request.hasMinPriceOnly()) {
            return offerRepository.findByPhraseAndMinPrice(
                    request.getPhrase(),
                    request.getMinPrice(),
                    request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}