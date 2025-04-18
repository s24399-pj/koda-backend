package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.CreateOfferCommand;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.AppUserService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final AppUserService appUserService;

    @Transactional(readOnly = true)
    public OfferDto findOfferById(UUID id) {
        return offerRepository.findByIdWithDetails(id)
                .map(offerMapper::mapToOfferDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Article not found with id: ", id.toString())
                );
    }

    @Transactional(readOnly = true)
    public List<String> findOfferNamesByPhrase(String phrase) {
        return null;
    }

    @Transactional
    public OfferDto createOffer(CreateOfferCommand command, String userEmail) {
        AppUser user = appUserService.getUserByEmail(userEmail);

        Offer offer = offerMapper.mapToOffer(command);

        offer.setSeller(user);

        LocalDateTime now = LocalDateTime.now();

        if (offer.getExpirationDate() == null) {
            offer.setExpirationDate(now.plusDays(30));
        }

        offer.setViewCount(0);

        Offer savedOffer = offerRepository.save(offer);

        return offerMapper.mapToOfferDto(savedOffer);
    }

}
