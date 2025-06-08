package pl.pjwstk.kodabackend.offer.handler;

import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;

/**
 * Handler interface for processing offer search requests using Chain of Responsibility pattern.
 * <p>
 * Each handler can process the search request or pass it to the next handler in the chain.
 * Handlers are ordered using Spring's {@link Ordered} interface to control execution sequence.
 * </p>
 */
public interface OfferSearchHandler extends Ordered {

    /**
     * Processes the search request and returns paginated results.
     * <p>
     * Implementations should either handle the request completely or delegate
     * to the next handler in the chain if unable to process.
     * </p>
     *
     * @param request the search criteria and pagination parameters
     * @return paginated search results containing matching offers
     */
    Page<OfferMiniDto> handle(OfferSearchRequest request);

    /**
     * Sets the next handler in the responsibility chain.
     *
     * @param next the next handler to be called if this handler cannot process the request
     */
    void setNext(OfferSearchHandler next);
}