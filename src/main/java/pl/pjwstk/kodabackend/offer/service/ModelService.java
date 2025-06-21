package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelService {

    private final CarDetailsRepository carDetailsRepository;

    @Cacheable(value = "modelsByBrand", key = "#brand")
    public List<String> getModelsByBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return List.of();
        }

        log.debug("Fetching models for brand: {}", brand);
        return carDetailsRepository.findModelsByBrand(brand);
    }
}