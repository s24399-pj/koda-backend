package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

@Component
public class PhraseAndPriceSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public PhraseAndPriceSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasPhrase() && request.hasPriceRange()) {

            if (request.hasUserId()) {
                return offerRepository.findByPhraseAndPriceRangeAndUserId(
                        request.getPhrase(),
                        request.getMinPrice(),
                        request.getMaxPrice(),
                        request.getUserId(),
                        request.getPageable());
            } else {
                return offerRepository.findByPhraseAndPriceRange(
                        request.getPhrase(),
                        request.getMinPrice(),
                        request.getMaxPrice(),
                        request.getPageable());
            }
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
