package pl.pjwstk.kodabackend.security.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.security.token.model.Token;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
class LogoutService implements LogoutHandler {

    private static final Pattern BEARER_TOKEN_REGEX = Pattern.compile("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.([A-Za-z0-9-_]+)?$");
    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new BadCredentialsException("Authorization header is missing");
        }
        if (!authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Authorization header must start with Bearer");
        }

        String accessToken = authHeader.substring(7);
        if (!BEARER_TOKEN_REGEX.matcher(accessToken).matches()) {
            throw new BadCredentialsException("Access token format is invalid");
        }

        Token storedToken = tokenRepository.findByTokenValue(accessToken)
                .orElse(null);

        if (storedToken != null) {
            tokenRepository.delete(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
