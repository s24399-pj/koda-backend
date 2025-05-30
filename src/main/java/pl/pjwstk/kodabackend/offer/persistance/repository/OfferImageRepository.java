package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferImageRepository extends JpaRepository<OfferImage, UUID> {

    List<OfferImage> findByOfferIdOrderBySortOrderAsc(UUID offerId);

    @Query("SELECT COUNT(oi) FROM OfferImage oi WHERE oi.offer.id = :offerId")
    int countByOfferId(@Param("offerId") UUID offerId);

    List<OfferImage> findByOfferIdAndIsPrimaryTrue(UUID offerId);
}