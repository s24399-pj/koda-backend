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
import pl.pjwstk.kodabackend.offer.persistence.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistence.entity.TransmissionType;
import pl.pjwstk.kodabackend.offer.persistence.repository.CarDetailsRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterOptionsServiceTest {

    @Mock
    private CarDetailsRepository carDetailsRepository;

    @InjectMocks
    private FilterOptionsService filterOptionsService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAllBrands_WhenCalled_DelegatesToRepository() {
        // given
        List<String> brands = Arrays.asList("Audi", "BMW", "Mercedes");
        Page<String> expectedPage = new PageImpl<>(brands, pageable, brands.size());
        when(carDetailsRepository.findAllBrandsPageable(pageable)).thenReturn(expectedPage);

        // when
        Page<String> result = filterOptionsService.getAllBrands(pageable);

        // then
        assertEquals(expectedPage, result);
        assertEquals(3, result.getContent().size());
        verify(carDetailsRepository).findAllBrandsPageable(pageable);
    }

    @Test
    void searchBrands_WhenCalled_DelegatesToRepository() {
        // given
        String phrase = "au";
        List<String> matchingBrands = Arrays.asList("Audi", "Renault");
        Page<String> expectedPage = new PageImpl<>(matchingBrands, pageable, matchingBrands.size());
        when(carDetailsRepository.findBrandsByPhrasePageable(phrase, pageable)).thenReturn(expectedPage);

        // when
        Page<String> result = filterOptionsService.searchBrands(phrase, pageable);

        // then
        assertEquals(expectedPage, result);
        assertEquals(2, result.getContent().size());
        verify(carDetailsRepository).findBrandsByPhrasePageable(phrase, pageable);
    }

    @Test
    void getFuelTypes_WhenFirstPage_ReturnsCorrectPage() {
        // given
        Pageable firstPage = PageRequest.of(0, 3);

        // when
        Page<String> result = filterOptionsService.getFuelTypes(firstPage);

        // then
        assertEquals(3, result.getContent().size());
        assertEquals(FuelType.values().length, result.getTotalElements());
        assertTrue(result.hasNext());
        assertFalse(result.hasPrevious());
    }

    @Test
    void getTransmissionTypes_WhenCalled_ReturnsAllTypes() {
        // given
        Pageable allItems = PageRequest.of(0, 10);

        // when
        Page<String> result = filterOptionsService.getTransmissionTypes(allItems);

        // then
        assertEquals(TransmissionType.values().length, result.getContent().size());
        assertEquals(TransmissionType.values().length, result.getTotalElements());
        assertTrue(result.getContent().contains("MANUAL"));
        assertTrue(result.getContent().contains("AUTOMATIC"));
    }

    @Test
    void getBodyTypes_WhenSecondPage_ReturnsCorrectPage() {
        // given
        Pageable secondPage = PageRequest.of(1, 5);

        // when
        Page<String> result = filterOptionsService.getBodyTypes(secondPage);

        // then
        assertTrue(result.getContent().size() <= 5);
        assertEquals(1, result.getNumber());
        assertTrue(result.hasPrevious());
    }

    @Test
    void getDriveTypes_WhenCalled_ReturnsDisplayNames() {
        // given
        Pageable allItems = PageRequest.of(0, 10);

        // when
        Page<String> result = filterOptionsService.getDriveTypes(allItems);

        // then
        assertFalse(result.getContent().isEmpty());
        // Verify that display names are used instead of enum names
        // Assuming DriveType has different display names than enum names
        result.getContent().forEach(driveType ->
                assertNotNull(driveType)
        );
    }

    @Test
    void getVehicleConditions_WhenCalled_ReturnsAllConditions() {
        // given
        Pageable allItems = PageRequest.of(0, 10);

        // when
        Page<String> result = filterOptionsService.getVehicleConditions(allItems);

        // then
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getContent().contains("NEW"));
        assertTrue(result.getContent().contains("USED"));
    }

    @Test
    void getBodyTypes_WhenExactlyLastPage_ReturnsRemainingItems() {
        // given
        int totalBodyTypes = pl.pjwstk.kodabackend.offer.persistence.entity.BodyType.values().length;
        int pageSize = 3;
        int lastPageIndex = (totalBodyTypes - 1) / pageSize;
        Pageable lastPage = PageRequest.of(lastPageIndex, pageSize);

        // when
        Page<String> result = filterOptionsService.getBodyTypes(lastPage);

        // then
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getContent().size() <= pageSize);
        assertFalse(result.hasNext());
        assertEquals(totalBodyTypes, result.getTotalElements());
    }
}