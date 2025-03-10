package pl.pjwstk.kodabackend.offer.model;

import java.util.UUID;

public record CarEquipmentDto(
        UUID id,

        // Komfort
        Boolean airConditioning,
        Boolean automaticClimate,
        Boolean heatedSeats,
        Boolean electricSeats,
        Boolean leatherSeats,
        Boolean panoramicRoof,
        Boolean electricWindows,
        Boolean electricMirrors,
        Boolean keylessEntry,
        Boolean wheelHeating,

        // Multimedia
        Boolean navigationSystem,
        Boolean bluetooth,
        Boolean usbPort,
        Boolean multifunction,
        Boolean androidAuto,
        Boolean appleCarPlay,
        Boolean soundSystem,

        // Systemy wspomagające
        Boolean parkingSensors,
        Boolean rearCamera,
        Boolean cruiseControl,
        Boolean adaptiveCruiseControl,
        Boolean laneAssist,
        Boolean blindSpotDetection,
        Boolean emergencyBraking,
        Boolean startStop,

        // Oświetlenie
        Boolean xenonLights,
        Boolean ledLights,
        Boolean ambientLighting,
        Boolean automaticLights,
        Boolean adapativeLights,

        // Dodatkowe funkcje
        Boolean heatedSteeringWheel,
        Boolean electricTrunk,
        Boolean electricSunBlind,
        Boolean headUpDisplay,
        Boolean aromatherapy
) {
}