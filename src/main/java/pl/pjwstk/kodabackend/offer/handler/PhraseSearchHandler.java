package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

@Component
class PhraseSearchHandler extends BaseOfferSearchHandler {

    @Autowired
    public PhraseSearchHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        if (request.hasPhrase()) {
            return offerRepository.findByPhraseContainingIgnoreCase(
                    request.getPhrase(),
                    request.getPageable());
        }
        return processNext(request);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
