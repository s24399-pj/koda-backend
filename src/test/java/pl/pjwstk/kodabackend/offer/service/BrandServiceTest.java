package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private CarDetailsRepository carDetailsRepository;

    @InjectMocks
    private BrandService brandService;

    private List<String> allBrands;

    @BeforeEach
    void setUp() {
        allBrands = Arrays.asList("Audi", "BMW", "Mercedes", "Toyota", "Volkswagen");
    }

    @Test
    void getAllBrands_WhenCalled_ReturnsBrandsFromRepository() {
        // given
        when(carDetailsRepository.findAllBrands()).thenReturn(allBrands);

        // when
        List<String> result = brandService.getAllBrands();

        // then
        assertEquals(allBrands, result);
        assertEquals(5, result.size());
        verify(carDetailsRepository).findAllBrands();
    }

    @Test
    void findBrandsByPhrase_WhenPhraseIsNull_ReturnsAllBrands() {
        // given
        when(carDetailsRepository.findAllBrands()).thenReturn(allBrands);

        // when
        List<String> result = brandService.findBrandsByPhrase(null);

        // then
        assertEquals(allBrands, result);
        verify(carDetailsRepository).findAllBrands();
        verify(carDetailsRepository, never()).findBrandsByPhrase(anyString());
    }

    @Test
    void findBrandsByPhrase_WhenPhraseIsEmpty_ReturnsAllBrands() {
        // given
        when(carDetailsRepository.findAllBrands()).thenReturn(allBrands);

        // when
        List<String> result = brandService.findBrandsByPhrase("   ");

        // then
        assertEquals(allBrands, result);
        verify(carDetailsRepository).findAllBrands();
        verify(carDetailsRepository, never()).findBrandsByPhrase(anyString());
    }

    @Test
    void findBrandsByPhrase_WhenPhraseHasTwoOrMoreCharacters_UsesRepository() {
        // given
        String phrase = "au";
        List<String> filteredBrands = Arrays.asList("Audi");
        when(carDetailsRepository.findBrandsByPhrase(phrase)).thenReturn(filteredBrands);

        // when
        List<String> result = brandService.findBrandsByPhrase(phrase);

        // then
        assertEquals(filteredBrands, result);
        assertEquals(1, result.size());
        assertEquals("Audi", result.get(0));
        verify(carDetailsRepository).findBrandsByPhrase(phrase);
        verify(carDetailsRepository, never()).findAllBrands();
    }


    @Test
    void findBrandsByPhrase_WhenNoBrandsMatch_ReturnsEmptyList() {
        // given
        String phrase = "xyz";
        when(carDetailsRepository.findBrandsByPhrase(phrase)).thenReturn(Collections.emptyList());

        // when
        List<String> result = brandService.findBrandsByPhrase(phrase);

        // then
        assertTrue(result.isEmpty());
        verify(carDetailsRepository).findBrandsByPhrase(phrase);
    }
}