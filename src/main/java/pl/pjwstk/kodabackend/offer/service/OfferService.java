package pl.pjwstk.kodabackend.offer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.offer.mapper.OfferMapper;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

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

}
