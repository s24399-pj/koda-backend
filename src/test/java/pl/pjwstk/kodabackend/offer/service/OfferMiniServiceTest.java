package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.util.ReflectionTestUtils;
import pl.pjwstk.kodabackend.offer.handler.OfferSearchHandler;
import pl.pjwstk.kodabackend.offer.model.OfferMiniDto;
import pl.pjwstk.kodabackend.offer.model.OfferSearchRequest;
import pl.pjwstk.kodabackend.offer.persistence.entity.FuelType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferMiniServiceTest {

    @Mock
    private OfferSearchHandler firstHandler;

    @Mock
    private OfferSearchHandler secondHandler;

    @Mock
    private OfferSearchHandler thirdHandler;

    @Spy
    private Set<OfferSearchHandler> handlers = new HashSet<>();

    @InjectMocks
    private OfferMiniService offerMiniService;

    private final OfferMiniDto sampleOffer = new OfferMiniDto(
            UUID.randomUUID(),
            "Test Car",
            BigDecimal.valueOf(10000),
            "image-url.jpg",
            50000,
            FuelType.PETROL,
            2018,
            150,
            "2.0"
    );

    @BeforeEach
    void setUp() {
        lenient().when(firstHandler.getOrder()).thenReturn(1);
        lenient().when(secondHandler.getOrder()).thenReturn(2);
        lenient().when(thirdHandler.getOrder()).thenReturn(3);

        handlers.add(firstHandler);
        handlers.add(secondHandler);
        handlers.add(thirdHandler);
    }

    @Test
    void initializeChain_shouldSortAndLinkHandlers() {
        offerMiniService.initializeChain();

        verify(firstHandler).setNext(secondHandler);
        verify(secondHandler).setNext(thirdHandler);

        OfferSearchHandler actualFirstHandler = (OfferSearchHandler) ReflectionTestUtils.getField(offerMiniService, "firstHandler");
        assertThat(actualFirstHandler).isEqualTo(firstHandler);
    }

    @Test
    void initializeChain_shouldHandleEmptyHandlers() {
        handlers.clear();

        offerMiniService.initializeChain();

        OfferSearchHandler actualFirstHandler = (OfferSearchHandler) ReflectionTestUtils.getField(offerMiniService, "firstHandler");
        assertThat(actualFirstHandler).isNull();
    }

    @Test
    void findAllOfferMini_shouldCallHandlerWithCorrectParameters() {
        offerMiniService.initializeChain();

        Pageable pageable = PageRequest.of(0, 10);
        String phrase = "test phrase";
        BigDecimal minPrice = BigDecimal.valueOf(5000);
        BigDecimal maxPrice = BigDecimal.valueOf(15000);
        UUID userId = null;

        List<OfferMiniDto> offers = Collections.singletonList(sampleOffer);
        Page<OfferMiniDto> expectedPage = new PageImpl<>(offers, pageable, offers.size());

        when(firstHandler.handle(any())).thenReturn(expectedPage);

        Page<OfferMiniDto> result = offerMiniService.findAllOfferMini(pageable, phrase, minPrice, maxPrice, userId);

        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).containsExactly(sampleOffer);

        verify(firstHandler).handle(any(OfferSearchRequest.class));
    }

    @Test
    void findAllOfferMini_shouldTrimSearchPhrase() {
        offerMiniService.initializeChain();

        Pageable pageable = PageRequest.of(0, 10);
        String phrase = "  test phrase with spaces  ";
        UUID userId = null;

        when(firstHandler.handle(any())).thenAnswer(invocation -> {
            OfferSearchRequest request = invocation.getArgument(0);
            assertThat(request.getPhrase()).isEqualTo("test phrase with spaces");
            return Page.empty();
        });

        offerMiniService.findAllOfferMini(pageable, phrase, null, null, userId);

        verify(firstHandler).handle(any(OfferSearchRequest.class));
    }

    @Test
    void findAllOfferMini_shouldHandleNullFirstHandler() {
        ReflectionTestUtils.setField(offerMiniService, "firstHandler", null);
        Pageable pageable = PageRequest.of(0, 10);
        UUID userId = null;

        Page<OfferMiniDto> result = offerMiniService.findAllOfferMini(pageable, null, null, null, userId);

        assertThat(result).isEmpty();
    }

    @Test
    void sortingAliasProcessor_shouldProcessSortingAlias() {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Pageable result = OfferMiniService.sortingAliasProcessor(pageable);

        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getSort()).isInstanceOf(JpaSort.class);
        assertThat(result.getSort().toString()).contains("(price): DESC");
    }

    @Test
    void sortingAliasProcessor_shouldReturnOriginalPageableForUnsortedPageable() {
        Pageable pageable = PageRequest.of(0, 10);

        Pageable result = OfferMiniService.sortingAliasProcessor(pageable);

        assertThat(result).isSameAs(pageable);
    }

    @Test
    void sortingAliasProcessor_shouldReturnOriginalPageableForMultipleSortConditions() {
        Sort sort = Sort.by(Sort.Direction.DESC, "price").and(Sort.by(Sort.Direction.ASC, "year"));
        Pageable pageable = PageRequest.of(0, 10, sort);

        Pageable result = OfferMiniService.sortingAliasProcessor(pageable);

        assertThat(result).isSameAs(pageable);
    }

    @Test
    void sortingAliasProcessor_shouldReturnOriginalPageableForInvalidSortFormat() {
        Sort mockSort = mock(Sort.class);
        when(mockSort.isUnsorted()).thenReturn(false);
        when(mockSort.get()).thenReturn(Collections.singletonList(Sort.Order.desc("price")).stream());
        when(mockSort.toString()).thenReturn("invalid-format-without-colon");

        Pageable pageable = PageRequest.of(0, 10, mockSort);

        Pageable result = OfferMiniService.sortingAliasProcessor(pageable);

        assertThat(result).isSameAs(pageable);
    }
}