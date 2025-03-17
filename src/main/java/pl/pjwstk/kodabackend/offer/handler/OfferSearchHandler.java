package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;

public interface OfferSearchHandler extends Ordered {
    Page<OfferMiniDto> handle(OfferSearchRequest request);
    void setNext(OfferSearchHandler next);
}
