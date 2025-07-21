package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelServiceTest {

    @Mock
    private CarDetailsRepository carDetailsRepository;

    @InjectMocks
    private ModelService modelService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getModelsByBrand_WhenBrandIsValid_ReturnsModelsFromRepository() {
        // given
        String brand = "BMW";
        List<String> models = Arrays.asList("X5", "X3", "Series 3", "Series 5");
        Page<String> expectedPage = new PageImpl<>(models, pageable, models.size());
        when(carDetailsRepository.findModelsByBrand(brand, pageable)).thenReturn(expectedPage);

        // when
        Page<String> result = modelService.getModelsByBrand(brand, pageable);

        // then
        assertEquals(expectedPage, result);
        assertEquals(4, result.getContent().size());
        assertTrue(result.getContent().contains("X5"));
        verify(carDetailsRepository).findModelsByBrand(brand, pageable);
    }

    @Test
    void getModelsByBrand_WhenBrandIsNull_ReturnsEmptyPage() {
        // when
        Page<String> result = modelService.getModelsByBrand(null, pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verifyNoInteractions(carDetailsRepository);
    }

    @Test
    void getModelsByBrand_WhenBrandIsEmpty_ReturnsEmptyPage() {
        // when
        Page<String> result = modelService.getModelsByBrand("", pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verifyNoInteractions(carDetailsRepository);
    }

    @Test
    void getModelsByBrand_WhenBrandIsOnlyWhitespace_ReturnsEmptyPage() {
        // when
        Page<String> result = modelService.getModelsByBrand("   ", pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verifyNoInteractions(carDetailsRepository);
    }

    @Test
    void getModelsByBrand_WhenRepositoryReturnsEmptyPage_ReturnsEmptyPage() {
        // given
        String brand = "NonExistingBrand";
        Page<String> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(carDetailsRepository.findModelsByBrand(brand, pageable)).thenReturn(emptyPage);

        // when
        Page<String> result = modelService.getModelsByBrand(brand, pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(carDetailsRepository).findModelsByBrand(brand, pageable);
    }

    @Test
    void getModelsByBrand_WhenCalledMultipleTimes_CallsRepositoryEachTime() {
        // given
        String brand = "Audi";
        List<String> models = Arrays.asList("A4", "A6", "Q5");
        Page<String> expectedPage = new PageImpl<>(models, pageable, models.size());
        when(carDetailsRepository.findModelsByBrand(brand, pageable)).thenReturn(expectedPage);

        // when
        Page<String> result1 = modelService.getModelsByBrand(brand, pageable);
        Page<String> result2 = modelService.getModelsByBrand(brand, pageable);

        // then
        assertEquals(result1, result2);
        verify(carDetailsRepository, times(2)).findModelsByBrand(brand, pageable);
    }
}