package pl.pjwstk.kodabackend.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.offer.persistance.repository.CarDetailsRepository;
import pl.pjwstk.kodabackend.offer.persistance.repository.CarEquipmentRepository;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.offer.persistance.repository.OfferRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final CarDetailsRepository carDetailsRepository;
    private final CarEquipmentRepository carEquipmentRepository;
    private final OfferImageRepository offerImageRepository;
    private final OfferRepository offerRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        findOfferById(UUID.randomUUID());
    }

    @Transactional(readOnly = true)
    public void findOfferById(UUID id) {
        offerRepository.findById(id);
    }


}
