package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    @Query("SELECT o FROM Offer o " +
            "LEFT JOIN FETCH o.carDetails cd " +
            "LEFT JOIN FETCH cd.carEquipment " +
            "LEFT JOIN FETCH o.images " +
            "WHERE o.id = :id")
    Optional<Offer> findByIdWithDetails(@Param("id") UUID id);

}
