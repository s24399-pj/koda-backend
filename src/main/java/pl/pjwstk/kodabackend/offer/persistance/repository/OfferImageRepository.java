package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;

import java.util.UUID;

@Repository
public interface OfferImageRepository extends JpaRepository<OfferImage, UUID> {

}
