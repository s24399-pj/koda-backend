package pl.pjwstk.kodabackend.offer.persistance.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.pjwstk.kodabackend.offer.persistance.entity.DriveType;

import java.util.Arrays;

@Converter(autoApply = true)
public class DriveTypeConverter implements AttributeConverter<DriveType, String> {

    @Override
    public String convertToDatabaseColumn(DriveType driveType) {
        if (driveType == null) {
            return null;
        }
        return driveType.getDisplayName();
    }

    @Override
    public DriveType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Arrays.stream(DriveType.values())
                .filter(e -> e.getDisplayName().equals(dbData))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown database value: " + dbData));
    }
}