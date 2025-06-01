package pl.pjwstk.kodabackend.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class EntityNotFoundException extends RuntimeException {
    String name;
    String id;
}
