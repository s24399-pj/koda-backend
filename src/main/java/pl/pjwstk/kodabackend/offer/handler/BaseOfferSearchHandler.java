package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.data.domain.Page;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;

public abstract class BaseOfferSearchHandler implements OfferSearchHandler {
    protected final OfferRepository offerRepository;
    private OfferSearchHandler next;

    protected BaseOfferSearchHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public void setNext(OfferSearchHandler next) {
        this.next = next;
    }

    /**
     * Passes the request to the next handler in the chain
     *
     * @param request Search parameters
     * @return Page of results from the next handler or empty page
     */
    protected Page<OfferMiniDto> processNext(OfferSearchRequest request) {
        if (next != null) {
            return next.handle(request);
        }
        return Page.empty();
    }
}
