package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AdvancedOfferRepository extends JpaRepository<Offer, UUID> {

    @Query(value = """
        SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(
            o.id, o.title, o.price,
            (SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true),
            cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement
        )
        FROM Offer o
        JOIN o.carDetails cd
        WHERE 1=1
        AND (:searchTerm IS NULL OR 
             LOWER(o.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
             LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
             LOWER(cd.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
             LOWER(cd.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        AND (:priceFrom IS NULL OR o.price >= :priceFrom)
        AND (:priceTo IS NULL OR o.price <= :priceTo)
        AND (:brand IS NULL OR LOWER(cd.brand) = LOWER(:brand))
        AND (:model IS NULL OR LOWER(cd.model) = LOWER(:model))
        AND (:yearFrom IS NULL OR cd.year >= :yearFrom)
        AND (:yearTo IS NULL OR cd.year <= :yearTo)
        AND (:mileageFrom IS NULL OR cd.mileage >= :mileageFrom)
        AND (:mileageTo IS NULL OR cd.mileage <= :mileageTo)
        AND (:enginePowerFrom IS NULL OR cd.enginePower >= :enginePowerFrom)
        AND (:enginePowerTo IS NULL OR cd.enginePower <= :enginePowerTo)
        """)
    Page<OfferMiniDto> findOffersWithBasicFilters(
            @Param("searchTerm") String searchTerm,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            @Param("mileageFrom") Integer mileageFrom,
            @Param("mileageTo") Integer mileageTo,
            @Param("enginePowerFrom") Integer enginePowerFrom,
            @Param("enginePowerTo") Integer enginePowerTo,
            Pageable pageable
    );

    @Query(value = """
        SELECT new pl.pjwstk.kodabackend.offer.model.OfferMiniDto(
            o.id, o.title, o.price,
            (SELECT img.url FROM OfferImage img WHERE img.offer = o AND img.isPrimary = true),
            cd.mileage, cd.fuelType, cd.year, cd.enginePower, cd.displacement
        )
        FROM Offer o
        JOIN o.carDetails cd
        LEFT JOIN cd.carEquipment ce
        WHERE 1=1
        AND (:searchTerm IS NULL OR 
             LOWER(o.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
             LOWER(o.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        AND (:priceFrom IS NULL OR o.price >= :priceFrom)
        AND (:priceTo IS NULL OR o.price <= :priceTo)
        AND (:brand IS NULL OR LOWER(cd.brand) = LOWER(:brand))
        AND (:airConditioning IS NULL OR ce.airConditioning = :airConditioning)
        AND (:navigationSystem IS NULL OR ce.navigationSystem = :navigationSystem)
        AND (:bluetooth IS NULL OR ce.bluetooth = :bluetooth)
        """)
    Page<OfferMiniDto> findOffersWithEquipmentFilters(
            @Param("searchTerm") String searchTerm,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("priceTo") BigDecimal priceTo,
            @Param("brand") String brand,
            @Param("airConditioning") Boolean airConditioning,
            @Param("navigationSystem") Boolean navigationSystem,
            @Param("bluetooth") Boolean bluetooth,
            Pageable pageable
    );

    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd ORDER BY cd.brand")
    List<String> findAllBrands();

    @Query("SELECT DISTINCT cd.model FROM CarDetails cd WHERE LOWER(cd.brand) = LOWER(:brand) ORDER BY cd.model")
    List<String> findModelsByBrand(@Param("brand") String brand);
}