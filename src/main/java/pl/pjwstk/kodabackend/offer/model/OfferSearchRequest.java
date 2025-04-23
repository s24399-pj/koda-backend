package pl.pjwstk.kodabackend.offer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents search parameters for offer search operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferSearchRequest {
    private String phrase;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Pageable pageable;
    private UUID userId;

    /**
     * Checks if a user ID filter is provided.
     *
     * @return true if user ID is provided
     */
    public boolean hasUserId() {
        return userId != null;
    }

    /**
     * Checks if a search phrase is provided.
     *
     * @return true if a non-empty phrase is available
     */
    public boolean hasPhrase() {
        return phrase != null && !phrase.trim().isEmpty();
    }

    /**
     * Checks if a complete price range (both min and max) is provided.
     *
     * @return true if both min and max price are available
     */
    public boolean hasPriceRange() {
        return minPrice != null && maxPrice != null;
    }

    /**
     * Checks if only minimum price is provided.
     *
     * @return true if only min price is available
     */
    public boolean hasMinPriceOnly() {
        return minPrice != null && maxPrice == null;
    }

    /**
     * Checks if only maximum price is provided.
     *
     * @return true if only max price is available
     */
    public boolean hasMaxPriceOnly() {
        return minPrice == null && maxPrice != null;
    }

    /**
     * Checks if any price filter is provided.
     *
     * @return true if either min price, max price, or both are provided
     */
    public boolean hasAnyPriceFilter() {
        return minPrice != null || maxPrice != null;
    }
}