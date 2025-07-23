package pl.pjwstk.kodabackend.advice;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import pl.pjwstk.kodabackend.exception.EntityConflictException;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private ListAppender<ILoggingEvent> logWatcher;
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logWatcher = new ListAppender<>();
        logWatcher.start();
        logger.addAppender(logWatcher);
    }

    @Nested
    @DisplayName("EntityNotFoundException handling")
    class EntityNotFoundExceptionHandling {

        @Test
        @DisplayName("Should handle EntityNotFoundException and return NotFoundMessage")
        void shouldHandleEntityNotFoundException() {
            // Given
            String entityName = "User";
            String entityId = "123";
            EntityNotFoundException exception = new EntityNotFoundException(entityName, entityId);

            // When
            NotFoundMessage result = globalExceptionHandler.handleNotFoundAuthorException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(entityName);
            assertThat(result.id()).isEqualTo(entityId);
        }

        @Test
        @DisplayName("Should handle EntityNotFoundException with null values")
        void shouldHandleEntityNotFoundExceptionWithNullValues() {
            // Given
            EntityNotFoundException exception = new EntityNotFoundException(null, null);

            // When
            NotFoundMessage result = globalExceptionHandler.handleNotFoundAuthorException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isNull();
            assertThat(result.id()).isNull();
        }
    }

    @Nested
    @DisplayName("EntityConflictException handling")
    class EntityConflictExceptionHandling {

        @Test
        @DisplayName("Should handle EntityConflictException and return EntityIssueMessage")
        void shouldHandleEntityConflictException() {
            // Given
            String errorMessage = "Entity already exists";
            EntityConflictException exception = new EntityConflictException(errorMessage);

            // When
            EntityIssueMessage result = globalExceptionHandler.handleConflictException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.message()).isEqualTo(errorMessage);
        }

        @Test
        @DisplayName("Should handle EntityConflictException with null message")
        void shouldHandleEntityConflictExceptionWithNullMessage() {
            // Given
            EntityConflictException exception = new EntityConflictException(null);

            // When
            EntityIssueMessage result = globalExceptionHandler.handleConflictException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.message()).isNull();
        }
    }

    @Nested
    @DisplayName("AccessDeniedException handling")
    class AccessDeniedExceptionHandling {

        @Test
        @DisplayName("Should handle AccessDeniedException and return EntityIssueMessage with Polish message")
        void shouldHandleAccessDeniedException() {
            // Given
            String originalMessage = "Access is denied";
            AccessDeniedException exception = new AccessDeniedException(originalMessage);

            // When
            EntityIssueMessage result = globalExceptionHandler.handleAccessDeniedException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.message()).isEqualTo("Brak uprawnień do wykonania tej operacji");
        }

        @Test
        @DisplayName("Should log warning message when handling AccessDeniedException")
        void shouldLogWarningWhenHandlingAccessDeniedException() {
            // Given
            String originalMessage = "Access is denied";
            AccessDeniedException exception = new AccessDeniedException(originalMessage);

            // When
            globalExceptionHandler.handleAccessDeniedException(exception);

            // Then
            assertThat(logWatcher.list).hasSize(1);
            ILoggingEvent logEvent = logWatcher.list.get(0);
            assertThat(logEvent.getLevel()).isEqualTo(Level.WARN);
            assertThat(logEvent.getFormattedMessage()).isEqualTo("Access denied: " + originalMessage);
        }

        @Test
        @DisplayName("Should handle AccessDeniedException with null message")
        void shouldHandleAccessDeniedExceptionWithNullMessage() {
            // Given
            AccessDeniedException exception = new AccessDeniedException(null);

            // When
            EntityIssueMessage result = globalExceptionHandler.handleAccessDeniedException(exception);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.message()).isEqualTo("Brak uprawnień do wykonania tej operacji");

            // Verify logging
            assertThat(logWatcher.list).hasSize(1);
            ILoggingEvent logEvent = logWatcher.list.get(0);
            assertThat(logEvent.getLevel()).isEqualTo(Level.WARN);
            assertThat(logEvent.getFormattedMessage()).isEqualTo("Access denied: null");
        }
    }
}