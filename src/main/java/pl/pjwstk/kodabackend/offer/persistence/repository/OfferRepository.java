package pl.pjwstk.kodabackend.offer.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID>, JpaSpecificationExecutor<Offer> {

    @Query("SELECT o FROM Offer o " +
            "LEFT JOIN FETCH o.carDetails cd " +
            "LEFT JOIN FETCH cd.carEquipment " +
            "LEFT JOIN FETCH o.images " +
            "LEFT JOIN FETCH o.seller " +
            "WHERE o.id = :id")
    Optional<Offer> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))")
    Page<OfferMiniDto> findByPhraseContainingIgnoreCase(@Param("phrase") String phrase, Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE o.price BETWEEN :minPrice AND :maxPrice")
    Page<OfferMiniDto> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE o.price >= :minPrice")
    Page<OfferMiniDto> findByMinPriceOnly(@Param("minPrice") BigDecimal minPrice, Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE o.price <= :maxPrice")
    Page<OfferMiniDto> findByMaxPriceOnly(@Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE (LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))) AND " +
            "o.price >= :minPrice")
    Page<OfferMiniDto> findByPhraseAndMinPrice(
            @Param("phrase") String phrase,
            @Param("minPrice") BigDecimal minPrice,
            Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE (LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))) AND " +
            "o.price <= :maxPrice")
    Page<OfferMiniDto> findByPhraseAndMaxPrice(
            @Param("phrase") String phrase,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "WHERE (LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))) AND " +
            "o.price BETWEEN :minPrice AND :maxPrice")
    Page<OfferMiniDto> findByPhraseAndPriceRange(
            @Param("phrase") String phrase,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd")
    Page<OfferMiniDto> findAllMiniDto(Pageable pageable);

    @Query("SELECT DISTINCT o.title FROM Offer o WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) ORDER BY o.title")
    List<String> findDistinctTitlesByPhrase(@Param("phrase") String phrase);

    /**
     * Finds all offers by owner/user ID.
     */
    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "JOIN o.seller s " +
            "WHERE s.id = :userId")
    Page<OfferMiniDto> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * Finds all offers by phrase and owner/user ID.
     */
    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "JOIN o.seller s " +
            "WHERE (LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))) AND " +
            "s.id = :userId")
    Page<OfferMiniDto> findByPhraseAndUserId(
            @Param("phrase") String phrase,
            @Param("userId") UUID userId,
            Pageable pageable);

    /**
     * Finds all offers by phrase, price range and owner/user ID.
     */
    @Query("SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(" +
            "o.id, o.title, o.price, " +
            "(SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true), " +
            "cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement) " +
            "FROM Offer o " +
            "JOIN o.carDetails cd " +
            "JOIN o.seller s " +
            "WHERE (LOWER(o.title) LIKE LOWER(CONCAT('%', :phrase, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :phrase, '%'))) AND " +
            "o.price BETWEEN :minPrice AND :maxPrice AND " +
            "s.id = :userId")
    Page<OfferMiniDto> findByPhraseAndPriceRangeAndUserId(
            @Param("phrase") String phrase,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("userId") UUID userId,
            Pageable pageable);

}