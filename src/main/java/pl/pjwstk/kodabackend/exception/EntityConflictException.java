package pl.pjwstk.kodabackend.exception;

import lombok.Value;

@Value
public class EntityConflictException extends RuntimeException {
    String message;
}
