package pl.pjwstk.kodabackend.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class EntityConflictException extends RuntimeException {
    String message;
}
