package pl.pjwstk.kodabackend.image.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.pjwstk.kodabackend.exception.BadRequestException;
import pl.pjwstk.kodabackend.exception.NotFoundException;
import pl.pjwstk.kodabackend.image.model.ImageUploadResponse;
import pl.pjwstk.kodabackend.image.service.FileStorageService;
import pl.pjwstk.kodabackend.image.service.FileValidationService;
import pl.pjwstk.kodabackend.offer.persistence.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistence.entity.OfferImage;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferImageRepository;
import pl.pjwstk.kodabackend.offer.persistence.repository.OfferRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {

    private final OfferImageRepository offerImageRepository;
    private final AppUserRepository appUserRepository;
    private final OfferRepository offerRepository;
    private final FileValidationService validationService;
    private final FileStorageService storageService;

    @Transactional
    public List<ImageUploadResponse> uploadImagesForOffer(MultipartFile[] files, UUID offerId, String userEmail) {
        log.info("Starting upload of {} files for offer {} by user: {}", files.length, offerId, userEmail);

        validationService.validateUploadRequest(files);

        AppUser user = findUserByEmail(userEmail);
        Offer offer = findOfferById(offerId);
        validateUserOwnsOffer(user, offer);

        int existingImagesCount = offerImageRepository.countByOfferId(offerId);
        int nextSortOrder = existingImagesCount + 1;

        List<ImageUploadResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                log.debug("Processing file {}/{}: {}", i + 1, files.length, file.getOriginalFilename());

                String savedFilename = storageService.saveFile(file);
                String fileUrl = "/api/v1/images/view/" + savedFilename;

                boolean isFirstImageOverall = existingImagesCount == 0 && i == 0;

                OfferImage offerImage = OfferImage.builder()
                        .url(fileUrl)
                        .caption(file.getOriginalFilename())
                        .isPrimary(isFirstImageOverall)
                        .sortOrder(nextSortOrder + i)
                        .offer(offer)
                        .build();

                OfferImage saved = offerImageRepository.save(offerImage);

                responses.add(ImageUploadResponse.builder()
                        .id(saved.getId())
                        .url(saved.getUrl())
                        .filename(file.getOriginalFilename())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .sortOrder(saved.getSortOrder())
                        .build());

                log.info("Saved image: {} (ID: {}) for offer: {} by user: {}",
                        savedFilename, saved.getId(), offerId, userEmail);

            } catch (IOException e) {
                log.error("Error saving file: {} - {}", file.getOriginalFilename(), e.getMessage());
                throw new BadRequestException("Error saving file: " + file.getOriginalFilename());
            }
        }

        log.info("Completed upload of {} files for offer {}. Created {} records.",
                files.length, offerId, responses.size());
        return responses;
    }

    @Transactional
    public List<ImageUploadResponse> uploadStandaloneImages(MultipartFile[] files, String userEmail) {
        log.info("Starting upload of {} standalone files by user: {}", files.length, userEmail);

        validationService.validateUploadRequest(files);

        List<ImageUploadResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                log.debug("Processing file {}/{}: {}", i + 1, files.length, file.getOriginalFilename());

                String savedFilename = storageService.saveFile(file);
                String fileUrl = "/api/v1/images/view/" + savedFilename;

                OfferImage offerImage = OfferImage.builder()
                        .url(fileUrl)
                        .caption(file.getOriginalFilename())
                        .isPrimary(i == 0)
                        .sortOrder(i + 1)
                        .build();

                OfferImage saved = offerImageRepository.save(offerImage);

                responses.add(ImageUploadResponse.builder()
                        .id(saved.getId())
                        .url(saved.getUrl())
                        .filename(file.getOriginalFilename())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .sortOrder(saved.getSortOrder())
                        .build());

                log.info("Saved standalone image: {} (ID: {}) for user: {}",
                        savedFilename, saved.getId(), userEmail);

            } catch (IOException e) {
                log.error("Error saving file: {} - {}", file.getOriginalFilename(), e.getMessage());
                throw new BadRequestException("Error saving file: " + file.getOriginalFilename());
            }
        }

        log.info("Completed upload of {} standalone files. Created {} records.", files.length, responses.size());
        return responses;
    }

    private AppUser findUserByEmail(String userEmail) {
        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Offer findOfferById(UUID offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer not found"));
    }

    private void validateUserOwnsOffer(AppUser user, Offer offer) {
        if (!offer.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You don't have permission to add images to this offer");
        }
    }
}