package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

@Component
public class AllOffersHandler extends BaseOfferSearchHandler {

    @Autowired
    public AllOffersHandler(OfferRepository offerRepository) {
        super(offerRepository);
    }

    @Override
    public Page<OfferMiniDto> handle(OfferSearchRequest request) {
        return offerRepository.findAllMiniDto(request.getPageable());
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
