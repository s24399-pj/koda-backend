package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelService {

    private final CarDetailsRepository carDetailsRepository;

    @Cacheable(value = "modelsByBrand", key = "#brand")
    public Page<String> getModelsByBrand(String brand, Pageable pageable) {
        log.debug("Fetching models for brand: {}", brand);

        return Optional.ofNullable(brand)
                .filter(StringUtils::isNotBlank)
                .map(b -> {
                    log.debug("Retrieving models from repository for brand: {}", b);
                    return carDetailsRepository.findModelsByBrand(b, pageable);
                })
                .orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));
    }
}