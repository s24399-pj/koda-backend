package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

/**
 * Handler for searching offers with minimum price only filter.
 */
@Component
public class MinPriceSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public MinPriceSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasMinPriceOnly()) {
            return offerRepository.findByMinPriceOnly(
                    request.getMinPrice(),
                    request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}