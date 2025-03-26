package pl.pjwstk.kodabackend.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pjwstk.kodabackend.exception.EntityConflictException;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    NotFoundMessage handleNotFoundAuthorException(EntityNotFoundException ex) {
        return new NotFoundMessage(ex.getName(), ex.getId());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityConflictException.class)
    EntityIssueMessage handleConflictException(EntityConflictException ex) {
        return new EntityIssueMessage(ex.getMessage());
    }


}

