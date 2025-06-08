package pl.pjwstk.kodabackend.image.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.exception.NotFoundException;
import pl.pjwstk.kodabackend.image.service.FileStorageService;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteImageUseCase {

    private final OfferImageRepository offerImageRepository;
    private final AppUserRepository appUserRepository;
    private final FileStorageService storageService;

    @Transactional
    public void deleteImage(UUID imageId, String userEmail) {
        log.info("Deleting image {} by user: {}", imageId, userEmail);

        OfferImage image = findImageById(imageId);
        AppUser user = findUserByEmail(userEmail);
        validateUserCanDeleteImage(user, image);

        try {
            storageService.deleteFile(image.getUrl());
            offerImageRepository.delete(image);

            log.info("Deleted image: {} by user: {}", imageId, userEmail);
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            throw new BadRequestException("Error deleting file");
        }
    }

    private OfferImage findImageById(UUID imageId) {
        return offerImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    private AppUser findUserByEmail(String userEmail) {
        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void validateUserCanDeleteImage(AppUser user, OfferImage image) {
        if (image.getOffer() != null && !image.getOffer().getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You don't have permission to delete this image");
        }
    }
}