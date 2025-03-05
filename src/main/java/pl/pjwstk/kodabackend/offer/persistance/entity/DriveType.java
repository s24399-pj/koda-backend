package pl.pjwstk.kodabackend.offer.persistance.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing vehicle drive types.
 */
@Getter
@RequiredArgsConstructor
public enum DriveType {
    FOUR_WHEEL_DRIVE("4WD"),
    FRONT_WHEEL_DRIVE("FWD"),
    REAR_WHEEL_DRIVE("RWD"),
    ALL_WHEEL_DRIVE("AWD"),
    OTHER("Other");

    private final String displayName;
}
