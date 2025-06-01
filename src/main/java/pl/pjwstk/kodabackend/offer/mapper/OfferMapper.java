package pl.pjwstk.kodabackend.offer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.OfferDetailedDto;
import pl.pjwstk.kodabackend.offer.model.command.CreateOfferCommand;
import pl.pjwstk.kodabackend.offer.model.OfferDto;
import pl.pjwstk.kodabackend.offer.model.UserDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.CarDetails;
import pl.pjwstk.kodabackend.offer.persistance.entity.CarEquipment;
import pl.pjwstk.kodabackend.offer.persistance.entity.Offer;
import pl.pjwstk.kodabackend.offer.persistance.entity.OfferImage;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OfferMapper {
    private final CarDetailsMapper carDetailsMapper;

    public OfferDto mapToOfferDto(Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                carDetailsMapper.mapToCarDetailsDto(offer.getCarDetails()),
                extractImageUrls(offer.getImages()),
                offer.getPrice(),
                offer.getCurrency(),
                mapToUserDto(offer.getSeller()),
                offer.getLocation(),
                offer.getContactPhone(),
                offer.getContactEmail(),
                offer.getCreatedAt(),
                offer.getUpdatedAt(),
                offer.getExpirationDate(),
                offer.getViewCount(),
                offer.isFeatured(),
                offer.isNegotiable()
        );
    }

    /**
     * Maps an Offer entity to OfferDetailedDto for advanced search results
     */
    public OfferDetailedDto mapToOfferDetailedDto(Offer offer) {
        return new OfferDetailedDto(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                carDetailsMapper.mapToCarDetailsDto(offer.getCarDetails()),
                extractImageUrls(offer.getImages()),
                offer.getPrice(),
                offer.getCurrency(),
                mapToUserDto(offer.getSeller()),
                offer.getLocation(),
                offer.getContactPhone(),
                offer.getContactEmail(),
                offer.getCreatedAt(),
                offer.getUpdatedAt(),
                offer.getExpirationDate(),
                offer.getViewCount(),
                offer.isFeatured(),
                offer.isNegotiable()
        );
    }

    private UserDto mapToUserDto(AppUser seller) {
        if (seller == null) {
            return null;
        }

        String profilePictureBase64 = null;
        if (seller.getProfilePicture() != null && seller.getProfilePicture().length > 0) {
            try {
                profilePictureBase64 = Base64.getEncoder().encodeToString(seller.getProfilePicture());
            } catch (Exception e) {
            }
        }

        return new UserDto(
                seller.getId(),
                seller.getFirstName(),
                seller.getLastName(),
                seller.getEmail(),
                profilePictureBase64
        );
    }

    private List<String> extractImageUrls(List<OfferImage> images) {
        return images.stream()
                .map(OfferImage::getUrl)
                .toList();
    }

    public Offer mapToOffer(CreateOfferCommand command) {
        CarEquipment carEquipment = null;
        if (command.getEquipment() != null) {
            carEquipment = CarEquipment.builder()
                    .airConditioning(command.getEquipment().getAirConditioning())
                    .automaticClimate(command.getEquipment().getAutomaticClimate())
                    .heatedSeats(command.getEquipment().getHeatedSeats())
                    .electricSeats(command.getEquipment().getElectricSeats())
                    .leatherSeats(command.getEquipment().getLeatherSeats())
                    .panoramicRoof(command.getEquipment().getPanoramicRoof())
                    .electricWindows(command.getEquipment().getElectricWindows())
                    .electricMirrors(command.getEquipment().getElectricMirrors())
                    .keylessEntry(command.getEquipment().getKeylessEntry())
                    .wheelHeating(command.getEquipment().getWheelHeating())
                    .navigationSystem(command.getEquipment().getNavigationSystem())
                    .bluetooth(command.getEquipment().getBluetooth())
                    .usbPort(command.getEquipment().getUsbPort())
                    .multifunction(command.getEquipment().getMultifunction())
                    .androidAuto(command.getEquipment().getAndroidAuto())
                    .appleCarPlay(command.getEquipment().getAppleCarPlay())
                    .soundSystem(command.getEquipment().getSoundSystem())
                    .parkingSensors(command.getEquipment().getParkingSensors())
                    .rearCamera(command.getEquipment().getRearCamera())
                    .cruiseControl(command.getEquipment().getCruiseControl())
                    .adaptiveCruiseControl(command.getEquipment().getAdaptiveCruiseControl())
                    .laneAssist(command.getEquipment().getLaneAssist())
                    .blindSpotDetection(command.getEquipment().getBlindSpotDetection())
                    .emergencyBraking(command.getEquipment().getEmergencyBraking())
                    .startStop(command.getEquipment().getStartStop())
                    .xenonLights(command.getEquipment().getXenonLights())
                    .ledLights(command.getEquipment().getLedLights())
                    .ambientLighting(command.getEquipment().getAmbientLighting())
                    .automaticLights(command.getEquipment().getAutomaticLights())
                    .adaptiveLights(command.getEquipment().getAdaptiveLights())
                    .heatedSteeringWheel(command.getEquipment().getHeatedSteeringWheel())
                    .electricTrunk(command.getEquipment().getElectricTrunk())
                    .electricSunBlind(command.getEquipment().getElectricSunBlind())
                    .headUpDisplay(command.getEquipment().getHeadUpDisplay())
                    .aromatherapy(command.getEquipment().getAromatherapy())
                    .build();
        }

        CarDetails carDetails = CarDetails.builder()
                .brand(command.getBrand())
                .model(command.getModel())
                .year(command.getYear())
                .color(command.getColor())
                .displacement(command.getDisplacement())
                .vin(command.getVin())
                .mileage(command.getMileage())
                .fuelType(command.getFuelType())
                .transmission(command.getTransmission())
                .bodyType(command.getBodyType())
                .driveType(command.getDriveType())
                .enginePower(command.getEnginePower())
                .doors(command.getDoors())
                .seats(command.getSeats())
                .condition(command.getCondition())
                .registrationNumber(command.getRegistrationNumber())
                .registrationCountry(command.getRegistrationCountry())
                .firstOwner(command.getFirstOwner())
                .accidentFree(command.getAccidentFree())
                .serviceHistory(command.getServiceHistory())
                .additionalFeatures(command.getAdditionalFeatures())
                .carEquipment(carEquipment)
                .build();

        Offer offer = Offer.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .price(command.getPrice())
                .currency(command.getCurrency())
                .location(command.getLocation())
                .contactPhone(command.getContactPhone())
                .contactEmail(command.getContactEmail())
                .expirationDate(command.getExpirationDate())
                .negotiable(command.isNegotiable())
                .carDetails(carDetails)
                .build();

        if (carDetails != null) {
            carDetails.setOffer(offer);
        }

        if (carEquipment != null && carDetails != null) {
            carEquipment.setCarDetails(carDetails);
        }

        return offer;
    }