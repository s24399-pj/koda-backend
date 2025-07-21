package pl.pjwstk.kodabackend.security.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pjwstk.kodabackend.security.token.model.Token;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private LogoutService logoutService;

    private String validToken;
    private Token storedToken;

    @BeforeEach
    void setUp() {
        validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        storedToken = new Token();
        storedToken.setTokenValue(validToken);
    }

    @Test
    void logout_WhenValidTokenExists_DeletesTokenAndClearsContext() {
        // given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(storedToken));

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            // when
            logoutService.logout(request, response, authentication);

            // then
            verify(tokenRepository).findByTokenValue(validToken);
            verify(tokenRepository).delete(storedToken);
            securityContextHolderMock.verify(SecurityContextHolder::clearContext);
        }
    }

    @Test
    void logout_WhenTokenNotInDatabase_DoesNotDeleteOrClearContext() {
        // given
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.empty());

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            // when
            logoutService.logout(request, response, authentication);

            // then
            verify(tokenRepository).findByTokenValue(validToken);
            verify(tokenRepository, never()).delete(any());
            securityContextHolderMock.verifyNoInteractions();
        }
    }

    @Test
    void logout_WhenAuthorizationHeaderMissing_ThrowsBadCredentialsException() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> logoutService.logout(request, response, authentication)
        );

        assertEquals("Authorization header is missing", exception.getMessage());
        verifyNoInteractions(tokenRepository);
    }

    @Test
    void logout_WhenAuthorizationHeaderDoesNotStartWithBearer_ThrowsBadCredentialsException() {
        // given
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> logoutService.logout(request, response, authentication)
        );

        assertEquals("Authorization header must start with Bearer", exception.getMessage());
        verifyNoInteractions(tokenRepository);
    }

    @Test
    void logout_WhenTokenFormatIsInvalid_ThrowsBadCredentialsException() {
        // given
        String invalidToken = "invalid-token-format";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> logoutService.logout(request, response, authentication)
        );

        assertEquals("Access token format is invalid", exception.getMessage());
        verifyNoInteractions(tokenRepository);
    }

    @Test
    void logout_WhenTokenHasOnlyTwoParts_ThrowsBadCredentialsException() {
        // given
        String twoPartToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + twoPartToken);

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> logoutService.logout(request, response, authentication)
        );

        assertEquals("Access token format is invalid", exception.getMessage());
        verifyNoInteractions(tokenRepository);
    }

    @Test
    void logout_WhenTokenHasSpecialCharacters_ThrowsBadCredentialsException() {
        // given
        String tokenWithSpecialChars = "eyJhbGci.eyJzdWI!@#$.SflKxwRJ";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenWithSpecialChars);

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> logoutService.logout(request, response, authentication)
        );

        assertEquals("Access token format is invalid", exception.getMessage());
        verifyNoInteractions(tokenRepository);
    }
}