package pl.pjwstk.kodabackend.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.AdvancedSearchRequest;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;
import pl.pjwstk.kodabackend.offer.persistence.entity.CarDetails;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdvancedOfferSearchServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private AdvancedOfferSearchService searchService;

    private Pageable pageable;
    private Offer offer1;
    private Offer offer2;
    private OfferDetailedDto offerDto1;
    private OfferDetailedDto offerDto2;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        offer1 = new Offer();
        offer1.setId(UUID.randomUUID());
        offer1.setTitle("BMW X5 2020");
        offer1.setPrice(new BigDecimal("100000"));

        offer2 = new Offer();
        offer2.setId(UUID.randomUUID());
        offer2.setTitle("Audi A4 2019");
        offer2.setPrice(new BigDecimal("80000"));

        offerDto1 = new OfferDetailedDto(
                offer1.getId(),
                offer1.getTitle(),
                "Description 1",
                null, // carDetails
                null, // imageUrls
                offer1.getPrice(),
                "PLN",
                null, // seller
                "Warsaw",
                "+48123456789",
                "seller1@example.com",
                null, // createdAt
                null, // updatedAt
                null, // expirationDate
                0,    // viewCount
                false,// featured
                true  // negotiable
        );

        offerDto2 = new OfferDetailedDto(
                offer2.getId(),
                offer2.getTitle(),
                "Description 2",
                null,
                null,
                offer2.getPrice(),
                "PLN",
                null,
                "Krakow",
                "+48987654321",
                "seller2@example.com",
                null,
                null,
                null,
                0,
                false,
                true
        );
    }

    @Test
    void searchOffers_WhenEmptyRequest_ReturnsAllOffers() {
        // given
        AdvancedSearchRequest request = new AdvancedSearchRequest();
        List<Offer> offers = Arrays.asList(offer1, offer2);
        Page<Offer> offerPage = new PageImpl<>(offers, pageable, 2);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer1)).thenReturn(offerDto1);
        when(offerMapper.mapToOfferDetailedDto(offer2)).thenReturn(offerDto2);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(offerRepository).findAll(any(Specification.class), eq(pageable));
        verify(offerMapper, times(2)).mapToOfferDetailedDto(any(Offer.class));
    }

    @Test
    void searchOffers_WhenPhraseProvided_FiltersResults() {
        // given
        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setPhrase("BMW");

        List<Offer> filteredOffers = Collections.singletonList(offer1);
        Page<Offer> offerPage = new PageImpl<>(filteredOffers, pageable, 1);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer1)).thenReturn(offerDto1);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        ArgumentCaptor<Specification<Offer>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(offerRepository).findAll(specCaptor.capture(), eq(pageable));
        assertNotNull(specCaptor.getValue());
    }

    @Test
    void searchOffers_WhenPriceRangeProvided_FiltersResults() {
        // given
        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setMinPrice(new BigDecimal("50000"));
        request.setMaxPrice(new BigDecimal("90000"));

        List<Offer> filteredOffers = Collections.singletonList(offer2);
        Page<Offer> offerPage = new PageImpl<>(filteredOffers, pageable, 1);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer2)).thenReturn(offerDto2);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(offerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchOffers_WhenUserIdProvided_FiltersResults() {
        // given
        UUID userId = UUID.randomUUID();
        AppUser user = new AppUser();
        user.setId(userId);
        offer1.setSeller(user);

        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setUserId(userId);

        List<Offer> filteredOffers = Collections.singletonList(offer1);
        Page<Offer> offerPage = new PageImpl<>(filteredOffers, pageable, 1);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer1)).thenReturn(offerDto1);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(offerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchOffers_WhenCarDetailsFiltersProvided_FiltersResults() {
        // given
        CarDetails carDetails = new CarDetails();
        carDetails.setBrand("BMW");
        carDetails.setModel("X5");
        carDetails.setYear(2020);
        offer1.setCarDetails(carDetails);

        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setBrand("BMW");
        request.setModel("X5");
        request.setMinYear(2019);
        request.setMaxYear(2021);

        List<Offer> filteredOffers = Collections.singletonList(offer1);
        Page<Offer> offerPage = new PageImpl<>(filteredOffers, pageable, 1);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer1)).thenReturn(offerDto1);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(offerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchOffers_WhenEquipmentFiltersProvided_FiltersResults() {
        // given
        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setAirConditioning(true);
        request.setNavigationSystem(true);
        request.setParkingSensors(true);

        List<Offer> filteredOffers = Collections.singletonList(offer1);
        Page<Offer> offerPage = new PageImpl<>(filteredOffers, pageable, 1);

        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(offerPage);
        when(offerMapper.mapToOfferDetailedDto(offer1)).thenReturn(offerDto1);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(offerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchOffers_WhenNoResults_ReturnsEmptyPage() {
        // given
        AdvancedSearchRequest request = new AdvancedSearchRequest();
        request.setPhrase("NonExistingCar");

        Page<Offer> emptyPage = Page.empty(pageable);
        when(offerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        // when
        Page<OfferDetailedDto> result = searchService.searchOffers(request, pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(offerMapper, never()).mapToOfferDetailedDto(any());
    }
}