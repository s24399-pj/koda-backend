package pl.pjwstk.kodabackend.offer.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistance.entity.FuelType;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceRangeSearchHandlerTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private BaseOfferSearchHandler nextHandler;

    private PriceRangeSearchHandler priceRangeSearchHandler;

    @BeforeEach
    void setUp() {
        priceRangeSearchHandler = new PriceRangeSearchHandler(offerRepository);
        priceRangeSearchHandler.setNext(nextHandler);
    }

    @Test
    void shouldReturnOffersByPriceRangeWhenRequestHasPriceRange() {
        BigDecimal minPrice = BigDecimal.valueOf(10000);
        BigDecimal maxPrice = BigDecimal.valueOf(50000);
        Pageable pageable = PageRequest.of(0, 10);

        OfferSearchRequest request = new OfferSearchRequest();
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setPageable(pageable);

        List<OfferMiniDto> offerList = Arrays.asList(
                createOfferMiniDto(1L),
                createOfferMiniDto(2L)
        );
        Page<OfferMiniDto> expectedPage = new PageImpl<>(offerList, pageable, offerList.size());

        when(offerRepository.findByPriceRange(minPrice, maxPrice, pageable)).thenReturn(expectedPage);

        Page<OfferMiniDto> result = priceRangeSearchHandler.handle(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(offerList);

        verify(offerRepository, times(1)).findByPriceRange(minPrice, maxPrice, pageable);
        verify(nextHandler, never()).handle(any());
    }

    @Test
    void shouldDelegateToNextHandlerWhenRequestDoesNotHavePriceRange() {
        OfferSearchRequest request = new OfferSearchRequest();
        request.setMinPrice(null);
        request.setMaxPrice(null);
        request.setPageable(PageRequest.of(0, 10));

        List<OfferMiniDto> offerList = Arrays.asList(
                createOfferMiniDto(3L),
                createOfferMiniDto(4L)
        );
        Page<OfferMiniDto> expectedPage = new PageImpl<>(offerList);

        when(nextHandler.handle(request)).thenReturn(expectedPage);

        Page<OfferMiniDto> result = priceRangeSearchHandler.handle(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(offerList);

        verify(offerRepository, never()).findByPriceRange(any(), any(), any());
        verify(nextHandler, times(1)).handle(request);
    }

    @Test
    void shouldDelegateToNextHandlerWhenOnlyMinPriceIsProvided() {
        OfferSearchRequest request = new OfferSearchRequest();
        request.setMinPrice(BigDecimal.valueOf(10000));
        request.setMaxPrice(null);
        request.setPageable(PageRequest.of(0, 10));

        List<OfferMiniDto> offerList = Arrays.asList(
                createOfferMiniDto(5L),
                createOfferMiniDto(6L)
        );
        Page<OfferMiniDto> expectedPage = new PageImpl<>(offerList);

        when(nextHandler.handle(request)).thenReturn(expectedPage);

        Page<OfferMiniDto> result = priceRangeSearchHandler.handle(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(offerList);

        verify(offerRepository, never()).findByPriceRange(any(), any(), any());
        verify(nextHandler, times(1)).handle(request);
    }

    @Test
    void shouldDelegateToNextHandlerWhenOnlyMaxPriceIsProvided() {
        OfferSearchRequest request = new OfferSearchRequest();
        request.setMinPrice(null);
        request.setMaxPrice(BigDecimal.valueOf(50000));
        request.setPageable(PageRequest.of(0, 10));

        List<OfferMiniDto> offerList = Arrays.asList(
                createOfferMiniDto(7L),
                createOfferMiniDto(8L)
        );
        Page<OfferMiniDto> expectedPage = new PageImpl<>(offerList);

        when(nextHandler.handle(request)).thenReturn(expectedPage);

        Page<OfferMiniDto> result = priceRangeSearchHandler.handle(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(offerList);

        verify(offerRepository, never()).findByPriceRange(any(), any(), any());
        verify(nextHandler, times(1)).handle(request);
    }

    @Test
    void getOrderShouldReturnTwo() {
        int order = priceRangeSearchHandler.getOrder();

        assertThat(order).isEqualTo(2);
    }

    private OfferMiniDto createOfferMiniDto(Long id) {
        return new OfferMiniDto(
                java.util.UUID.randomUUID(),
                "Test Offer " + id,
                BigDecimal.valueOf(10000 + id * 1000),
                "Test Brand",
                2020,
                FuelType.PETROL,
                100000,
                150,
                "url/to/image" + id
        );
    }
}


