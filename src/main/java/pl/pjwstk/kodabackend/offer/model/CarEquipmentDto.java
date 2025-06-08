package pl.pjwstk.kodabackend.offer.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(
        name = "CarEquipment",
        description = "Complete car equipment and features specification including comfort, multimedia, safety systems, lighting, and additional features"
)
public record CarEquipmentDto(
        @Schema(
                description = "Unique identifier of the car equipment configuration",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id,

        @Schema(description = "Air conditioning system", example = "true")
        Boolean airConditioning,

        @Schema(description = "Automatic climate control", example = "true")
        Boolean automaticClimate,

        @Schema(description = "Heated seats", example = "true")
        Boolean heatedSeats,

        @Schema(description = "Electric seat adjustment", example = "false")
        Boolean electricSeats,

        @Schema(description = "Leather seat upholstery", example = "true")
        Boolean leatherSeats,

        @Schema(description = "Panoramic sunroof", example = "false")
        Boolean panoramicRoof,

        @Schema(description = "Electric windows", example = "true")
        Boolean electricWindows,

        @Schema(description = "Electric mirrors", example = "true")
        Boolean electricMirrors,

        @Schema(description = "Keyless entry system", example = "true")
        Boolean keylessEntry,

        @Schema(description = "Heated steering wheel", example = "false")
        Boolean wheelHeating,

        // Multimedia
        @Schema(description = "Navigation system", example = "true")
        Boolean navigationSystem,

        @Schema(description = "Bluetooth connectivity", example = "true")
        Boolean bluetooth,

        @Schema(description = "USB port", example = "true")
        Boolean usbPort,

        @Schema(description = "Multifunction steering wheel", example = "true")
        Boolean multifunction,

        @Schema(description = "Android Auto support", example = "true")
        Boolean androidAuto,

        @Schema(description = "Apple CarPlay support", example = "true")
        Boolean appleCarPlay,

        @Schema(description = "Premium sound system", example = "false")
        Boolean soundSystem,

        // Assistance systems
        @Schema(description = "Parking sensors", example = "true")
        Boolean parkingSensors,

        @Schema(description = "Rear view camera", example = "true")
        Boolean rearCamera,

        @Schema(description = "Cruise control", example = "true")
        Boolean cruiseControl,

        @Schema(description = "Adaptive cruise control", example = "false")
        Boolean adaptiveCruiseControl,

        @Schema(description = "Lane keeping assist", example = "true")
        Boolean laneAssist,

        @Schema(description = "Blind spot detection", example = "false")
        Boolean blindSpotDetection,

        @Schema(description = "Emergency braking system", example = "true")
        Boolean emergencyBraking,

        @Schema(description = "Start-stop system", example = "true")
        Boolean startStop,

        // Lighting
        @Schema(description = "Xenon headlights", example = "false")
        Boolean xenonLights,

        @Schema(description = "LED headlights", example = "true")
        Boolean ledLights,

        @Schema(description = "Ambient lighting", example = "false")
        Boolean ambientLighting,

        @Schema(description = "Automatic headlights", example = "true")
        Boolean automaticLights,

        @Schema(description = "Adaptive headlights", example = "false")
        Boolean adaptiveLights,

        // Additional features
        @Schema(description = "Heated steering wheel", example = "false")
        Boolean heatedSteeringWheel,

        @Schema(description = "Electric trunk/tailgate", example = "false")
        Boolean electricTrunk,

        @Schema(description = "Electric sun blind", example = "false")
        Boolean electricSunBlind,

        @Schema(description = "Head-up display", example = "false")
        Boolean headUpDisplay,

        @Schema(description = "Aromatherapy system", example = "false")
        Boolean aromatherapy
) {
}