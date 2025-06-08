package pl.pjwstk.kodabackend.offer.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.offer.persistence.entity.LikedOffer;

import java.util.List;
import java.util.UUID;

public interface LikedOfferRepository extends JpaRepository<LikedOffer, UUID> {

    boolean existsByUserIdAndOfferId(UUID userId, UUID offerId);

    @Modifying
    @Query("DELETE FROM LikedOffer l WHERE l.userId = :userId AND l.offerId = :offerId")
    void deleteByUserIdAndOfferId(@Param("userId") UUID userId, @Param("offerId") UUID offerId);

    @Query("SELECT l.offerId FROM LikedOffer l WHERE l.userId = :userId")
    List<UUID> findOfferIdsByUserId(@Param("userId") UUID userId);
}