package pl.pjwstk.kodabackend.offer.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pjwstk.kodabackend.offer.persistance.entity.CarDetails;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarDetailsRepository extends JpaRepository<CarDetails, UUID> {


    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd ORDER BY cd.brand")
    List<String> findAllBrands();


    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd WHERE LOWER(cd.brand) LIKE LOWER(CONCAT('%', :phrase, '%')) ORDER BY cd.brand")
    List<String> findBrandsByPhrase(@Param("phrase") String phrase);
}