package pl.pjwstk.kodabackend.exception;

import lombok.Value;

@Value
public class EntityNotFoundException extends RuntimeException {
    String name;
    String id;
}
