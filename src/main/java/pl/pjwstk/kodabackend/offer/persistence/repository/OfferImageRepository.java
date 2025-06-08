package pl.pjwstk.kodabackend.offer.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;

import java.util.UUID;

public interface OfferImageRepository extends JpaRepository<OfferImage, UUID> {

    @Query("SELECT COUNT(oi) FROM OfferImage oi WHERE oi.offer.id = :offerId")
    int countByOfferId(@Param("offerId") UUID offerId);

}