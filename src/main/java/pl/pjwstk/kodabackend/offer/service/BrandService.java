package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final CarDetailsRepository carDetailsRepository;

    @Cacheable("allBrands")
    public List<String> getAllBrands() {
        log.debug("Fetching all unique car brands from the database");

        return carDetailsRepository.findAllBrands();
    }

    public List<String> findBrandsByPhrase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return getAllBrands();
        }

        log.debug("Searching for brands matching phrase: {}", phrase);
        String searchPhrase = phrase.toLowerCase();

        if (phrase.length() >= 2) {
            return carDetailsRepository.findBrandsByPhrase(searchPhrase);
        }

        return getAllBrands().stream().filter(brand -> brand.toLowerCase().contains(searchPhrase)).collect(Collectors.toList());
    }
}