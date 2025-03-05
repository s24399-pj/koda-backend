package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.CarEquipment;

import java.util.UUID;

@Repository
public interface CarEquipmentRepository extends JpaRepository<CarEquipment, UUID> {
}
