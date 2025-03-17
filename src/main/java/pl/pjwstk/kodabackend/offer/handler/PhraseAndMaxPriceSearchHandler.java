package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

/**
 * Handler for searching offers with both phrase and maximum price filter.
 */
@Component
public class PhraseAndMaxPriceSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public PhraseAndMaxPriceSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasPhrase() && request.hasMaxPriceOnly()) {
            return offerRepository.findByPhraseAndMaxPrice(
                    request.getPhrase(),
                    request.getMaxPrice(),
                    request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}