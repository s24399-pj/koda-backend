package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.data.domain.Page;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

public abstract class BaseOfferSearchHandler implements OfferSearchHandler {
    private OfferSearchHandler next;
    protected final OfferRepository offerRepository;

    protected BaseOfferSearchHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public void setNext(OfferSearchHandler next) {
        this.next = next;
    }

    /**
     * Przekazuje żądanie do następnego handlera w łańcuchu
     * @param request Parametry wyszukiwania
     * @return Strona wyników z następnego handlera lub pusta strona
     */
    protected Page<OfferMiniDto> processNext(OfferSearchRequest request) {
        if (next != null) {
            return next.handle(request);
        }
        return Page.empty();
    }
}
