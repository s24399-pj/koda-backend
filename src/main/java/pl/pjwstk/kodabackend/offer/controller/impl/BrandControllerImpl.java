package pl.pjwstk.kodabackend.offer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.offer.controller.BrandController;
import pl.pjwstk.kodabackend.offer.service.BrandService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
class BrandControllerImpl implements BrandController {

    private final BrandService brandService;

    @Override
    public List<String> getAllBrands() {
        log.info("Retrieving all available car brands");
        return brandService.getAllBrands();
    }

    @Override
    public List<String> findBrandsByPhrase(String phrase) {
        log.info("Searching for brands with phrase: {}", phrase);
        return brandService.findBrandsByPhrase(phrase);
    }
}