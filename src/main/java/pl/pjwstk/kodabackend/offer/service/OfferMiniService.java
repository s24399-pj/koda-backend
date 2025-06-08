package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.pjwstk.kodabackend.offer.handler.OfferSearchHandler;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Service responsible for searching offers in MiniDto form.
 * Uses Chain of Responsibility pattern to dynamically select search strategies.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OfferMiniService {

    private final Set<OfferSearchHandler> handlers;
    private OfferSearchHandler firstHandler;

    /**
     * Processes sorting aliases for queries.
     * Enables using custom fields for sorting.
     *
     * @param pageable Original pagination and sorting parameters
     * @return Modified pagination parameters with processed sorting aliases
     */
    public static Pageable sortingAliasProcessor(Pageable pageable) {
        if (pageable.getSort().isUnsorted() || pageable.getSort().get().toList().size() > 1) {
            return pageable;
        }

        String sortString = pageable.getSort().toString();
        int separatorIndex = sortString.indexOf(':');

        if (separatorIndex == -1) {
            return pageable;
        }

        String fieldName = sortString.substring(0, separatorIndex);
        String direction = sortString.substring(separatorIndex + 1).trim();

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                JpaSort.unsafe(
                        Sort.Direction.fromString(direction),
                        String.format("(%s)", fieldName)
                )
        );
    }

    /**
     * Initializes the search handlers chain after application startup.
     * Handlers are sorted by priority and linked into a chain.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeChain() {
        List<OfferSearchHandler> sortedHandlers = new ArrayList<>(handlers);
        sortedHandlers.sort(Comparator.comparingInt(OfferSearchHandler::getOrder));

        if (sortedHandlers.isEmpty()) {
            log.warn("No registered search handlers found!");
            return;
        }

        log.info("Initializing offer search chain with {} handlers", sortedHandlers.size());

        firstHandler = sortedHandlers.get(0);

        for (int i = 0; i < sortedHandlers.size() - 1; i++) {
            OfferSearchHandler current = sortedHandlers.get(i);
            OfferSearchHandler next = sortedHandlers.get(i + 1);

            current.setNext(next);
            log.debug("Handler {} -> {}", current.getClass().getSimpleName(), next.getClass().getSimpleName());
        }
    }

    /**
     * Searches for offers based on the provided parameters.
     * Uses a chain of handlers to select the most appropriate search strategy.
     *
     * @param pageable Pagination and sorting parameters
     * @param phrase   Search phrase (optional)
     * @param minPrice Minimum price (optional)
     * @param maxPrice Maximum price (optional)
     * @return Page of search results as OfferMiniDto
     */
    @Transactional(readOnly = true)
    public Page<OfferMiniDto> findAllOfferMini(
            Pageable pageable,
            @Nullable String phrase,
            @Nullable BigDecimal minPrice,
            @Nullable BigDecimal maxPrice,
            @Nullable UUID userId) {

        OfferSearchRequest searchRequest = buildSearchRequest(pageable, phrase, minPrice, maxPrice, userId);
        logSearchParameters(searchRequest);

        return Optional.ofNullable(firstHandler)
                .map(handler -> handler.handle(searchRequest))
                .orElseGet(() -> {
                    log.error("Handler chain has not been initialized!");
                    return Page.empty();
                });
    }

    /**
     * Creates a search request object based on the provided parameters.
     */
    private OfferSearchRequest buildSearchRequest(
            Pageable pageable,
            String phrase,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            UUID userId) {

        return OfferSearchRequest.builder()
                .phrase(StringUtils.hasText(phrase) ? phrase.trim() : null)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .pageable(pageable)
                .userId(userId)
                .build();
    }

    /**
     * Logs search parameters.
     */
    private void logSearchParameters(OfferSearchRequest request) {
        if (log.isDebugEnabled()) {
            StringBuilder logMessage = new StringBuilder("Searching offers with parameters: ");

            if (request.hasPhrase()) {
                logMessage.append("phrase='").append(request.getPhrase()).append("', ");
            }

            if (request.hasPriceRange()) {
                logMessage.append("price range=")
                        .append(request.getMinPrice())
                        .append("-")
                        .append(request.getMaxPrice())
                        .append(", ");
            } else if (request.getMinPrice() != null) {
                logMessage.append("minPrice=").append(request.getMinPrice()).append(", ");
            } else if (request.getMaxPrice() != null) {
                logMessage.append("maxPrice=").append(request.getMaxPrice()).append(", ");
            }

            if (request.hasUserId()) {
                logMessage.append("userId=").append(request.getUserId());
            }

            log.debug(logMessage.toString());
        }
    }
}