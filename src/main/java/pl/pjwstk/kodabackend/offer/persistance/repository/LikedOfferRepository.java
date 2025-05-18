package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.LikedOffer;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikedOfferRepository extends JpaRepository<LikedOffer, UUID> {

    List<LikedOffer> findByUserId(UUID userId);

    boolean existsByUserIdAndOfferId(UUID userId, UUID offerId);

    @Modifying
    @Query("DELETE FROM LikedOffer l WHERE l.userId = :userId AND l.offerId = :offerId")
    void deleteByUserIdAndOfferId(@Param("userId") UUID userId, @Param("offerId") UUID offerId);

    @Query("SELECT l.offerId FROM LikedOffer l WHERE l.userId = :userId")
    List<UUID> findOfferIdsByUserId(@Param("userId") UUID userId);
}