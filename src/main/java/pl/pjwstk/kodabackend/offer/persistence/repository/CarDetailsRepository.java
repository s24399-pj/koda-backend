package pl.pjwstk.kodabackend.offer.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjwstk.kodabackend.offer.persistence.entity.CarDetails;

import java.util.List;
import java.util.UUID;

public interface CarDetailsRepository extends JpaRepository<CarDetails, UUID> {

    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd ORDER BY cd.brand")
    List<String> findAllBrands();

    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd WHERE LOWER(cd.brand) LIKE LOWER(CONCAT('%', :phrase, '%')) ORDER BY cd.brand")
    List<String> findBrandsByPhrase(@Param("phrase") String phrase);

    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd ORDER BY cd.brand")
    Page<String> findAllBrandsPageable(Pageable pageable);

    @Query("SELECT DISTINCT cd.brand FROM CarDetails cd WHERE LOWER(cd.brand) LIKE LOWER(CONCAT('%', :phrase, '%')) ORDER BY cd.brand")
    Page<String> findBrandsByPhrasePageable(@Param("phrase") String phrase, Pageable pageable);
}